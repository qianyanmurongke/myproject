package com.course.core.web.fore;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.course.core.constant.Constants;
import com.course.core.domain.Site;
import com.course.core.support.Context;
import com.course.core.support.ForeContext;
import com.course.core.support.SiteResolver;

/**
 * SearchController
 * 
 * @author benfang
 * 
 */
@Controller
public class SearchController {
	public static final String TEMPLATE = "sys_search.html";

	@RequestMapping("/search")
	public String search(Integer page, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		return search(null, page, request, modelMap);
	}

	@RequestMapping(Constants.SITE_PREFIX_PATH + "/search")
	public String search(@PathVariable String siteNumber, Integer page,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		siteResolver.resolveSite(siteNumber);
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		ForeContext.setPage(data, page);
		return site.getTemplate(TEMPLATE);
	}

	@Autowired
	private SiteResolver siteResolver;
}
