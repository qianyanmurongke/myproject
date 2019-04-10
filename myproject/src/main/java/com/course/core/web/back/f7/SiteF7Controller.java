package com.course.core.web.back.f7;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.course.common.web.Servlets;
import com.course.core.domain.Site;
import com.course.core.service.SiteService;

/**
 * SiteF7Controller
 * 
 * @author benfang
 * 
 */
@Controller
@RequestMapping("/core/site")
public class SiteF7Controller {
	/**
	 * 站点单选。
	 * 
	 * @param id
	 * @param excludeChildrenId
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping("choose_site_tree.do")
	public String f7SiteTree(Integer id,
			@RequestParam(defaultValue = "true") Boolean allowRoot,
			Integer excludeChildrenId, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		List<Site> list = service.findList();
		Site bean = null, excludeChildrenBean = null;
		if (id != null) {
			bean = service.get(id);
		}
		if (excludeChildrenId != null) {
			excludeChildrenBean = service.get(excludeChildrenId);
		}

		modelMap.addAttribute("id", id);
		modelMap.addAttribute("allowRoot", allowRoot);
		modelMap.addAttribute("excludeChildrenId", excludeChildrenId);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("excludeChildrenBean", excludeChildrenBean);
		modelMap.addAttribute("list", list);
		Servlets.setNoCacheHeader(response);
		return "core/site/choose_site_tree";
	}

	@Autowired
	private SiteService service;
}
