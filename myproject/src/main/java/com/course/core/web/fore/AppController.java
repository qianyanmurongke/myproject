package com.course.core.web.fore;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.course.common.web.Servlets;
import com.course.core.constant.Constants;
import com.course.core.domain.Site;
import com.course.core.support.Context;
import com.course.core.support.ForeContext;
import com.course.core.support.Response;
import com.course.core.support.SiteResolver;

/**
 * AppController
 * 
 * @author benfang
 * 
 */
@Controller
public class AppController {

	@RequestMapping("/app")
	private String app(Integer page, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		return app(null, page, request, response, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/app")
	private String app(@PathVariable String siteNumber, Integer page,
			HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		Response resp = new Response(request, response, modelMap);
		String template = Servlets.getParam(request, "template");
		if (StringUtils.isBlank(template)) {
			return resp.badRequest("parameter 'template' is required.");
		}
		template = "app_" + template + ".html";

		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(template);
	}

	@Autowired
	private SiteResolver siteResolver;
}
