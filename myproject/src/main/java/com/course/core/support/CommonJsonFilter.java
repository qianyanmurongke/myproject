package com.course.core.support;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.course.common.util.BodyReaderHttpServletRequestWrapper;
import com.course.common.util.HttpHelper;
import com.course.common.util.JsonMapper;
import com.course.core.constant.Constants;

/**
 * 返回JSON。用于记录请求执行时间。
 * 
 * @author Fang YinLang
 * 
 */
public class CommonJsonFilter implements Filter {
	private static final Logger logger = LoggerFactory.getLogger(CommonJsonFilter.class);

	public static final String TIMER_BEGIN = "_timerBegin";
	public static final NumberFormat FORMAT = new DecimalFormat("0.000");

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		long begin = System.currentTimeMillis();
		request.setAttribute(TIMER_BEGIN, begin);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");

		response.setHeader("Content-Type", "application/json; charset=utf-8");
		response.setHeader("access-control-allow-headers", "x-requested-with,content-type");

		if ("post".equals(request.getMethod().toLowerCase())) {
			ServletRequest requestWrapper = new BodyReaderHttpServletRequestWrapper(request);
			String body = HttpHelper.getBodyString(requestWrapper);

			if (logger.isDebugEnabled()) {

				logger.debug("Post josn: {}", body);
			}
			
			if (request.getHeader(Constants.AUTHORIZATION) != null) {
				response.setHeader(Constants.AUTHORIZATION, request.getHeader(Constants.AUTHORIZATION));

			} 

			chain.doFilter(requestWrapper, response);

		} else {

			JsonMapper mapper = new JsonMapper();

			Map<String, Object> result = new HashMap<String, Object>();

			result.put("error", 500);

			result.put("message", "当前接口未授权,参数非法");
			String json = mapper.toJson(result);

			response.getWriter().write(json);
			// chain.doFilter(request, response);
		}

		if (logger.isDebugEnabled()) {
			long end = System.currentTimeMillis();
			BigDecimal processed = new BigDecimal(end - begin).divide(new BigDecimal(1000));
			String uri = ((HttpServletRequest) request).getRequestURI();
			logger.debug("Processed in {} second(s). URI={}", FORMAT.format(processed), uri);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void destroy() {
	}
}
