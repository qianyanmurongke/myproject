package com.course.core.support;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.course.core.domain.Site;
import com.course.core.service.GlobalService;
import com.course.core.service.SiteService;

public class SitePhoneInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// 从header中得到token
		// String authorization = request.getHeader(Constants.AUTHORIZATION);

		Site site = null;
		Context.setMobile(true);
		String serverName = request.getServerName();
		site = siteService.findByDomain(serverName);

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

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		Context.resetCurrentSite();
		Context.resetCurrentUser();
		Context.resetMobile();
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
