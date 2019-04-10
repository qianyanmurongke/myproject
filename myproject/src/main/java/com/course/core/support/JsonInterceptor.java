package com.course.core.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.course.core.domain.Site;
import com.course.core.service.GlobalService;
import com.course.core.service.SiteService;

public class JsonInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 如果不是映射到方法直接通过
		if (!(handler instanceof HandlerMethod)) {
			return true;
		}
		Site site = null;
		Context.setMobile(false);
		String serverName = request.getServerName();
		site = siteService.findByDomain(serverName);
		if (site != null) {
			// 域名与站点域名不相同，则为手机访问（手机端域名可能与PC端域名一样）
			if (!serverName.equals(site.getDomain())) {
				Context.setMobile(true);
			}
		}
		if (site == null) {
			site = globalService.findUnique().getSite();
		}
		if (site == null) {
			throw new CmsException("site.error.siteNotFound");
		}
		Context.setCurrentSite(site);

		Device device = deviceResolver.resolveDevice(request);
		// 手机域名存在，并且是手机客户端访问
		if (StringUtils.isNotBlank(site.getMobileDomain()) && device.isMobile()) {
			Context.setMobile(true);
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		Context.resetCurrentSite();
		Context.resetCurrentUser();
		Context.resetMobile();
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		/*
		 * if (modelAndView == null || !modelAndView.hasView()) { return; }
		 * ModelMap modelMap = modelAndView.getModelMap();
		 */

	}

	private DeviceResolver deviceResolver;
	private SiteService siteService;
	private GlobalService globalService;

	@Autowired
	public void setDeviceResolver(DeviceResolver deviceResolver) {
		this.deviceResolver = deviceResolver;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}

	@Autowired
	public void setGlobalService(GlobalService globalService) {
		this.globalService = globalService;
	}

}
