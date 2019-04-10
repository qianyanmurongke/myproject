package com.course.core.web.back;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.course.core.domain.Site;
import com.course.core.domain.User;
import com.course.core.holder.MenuHolder;
import com.course.core.support.Context;

/**
 * 后台首页
 * 
 * @author benfang
 * 
 */
@Controller
public class IndexController {
	/**
	 * 后台首页
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	@RequestMapping({ "/", "/index.do" })
	public String index(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Subject subject = SecurityUtils.getSubject();
		if (subject.isAuthenticated()) {
			Site site = Context.getCurrentSite();
			User user = Context.getCurrentUser();
//			List<Site> siteList = siteService.findByUserId(user.getId());
			modelMap.addAttribute("menus", menuHolder.getMenus());
			modelMap.addAttribute("user", user);
			modelMap.addAttribute("site", site);
//			modelMap.addAttribute("siteList", siteList);
			
			modelMap.addAttribute("orgList", user.getOrgs());
			modelMap.addAttribute("org", Context.getCurrentOrg(request));
			return "index";
		} else {
			return "login";
		}
	}

	/**
	 * 空白页。后台的左侧框架无内容时，使用空白页
	 * 
	 * @return
	 */
	@RequestMapping("/blank.do")
	public String blank() {
		return "blank";
	}

	@Autowired
	private MenuHolder menuHolder;
/*	@Autowired
	private SiteService siteService;*/

}
