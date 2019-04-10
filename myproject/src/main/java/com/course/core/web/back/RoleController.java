package com.course.core.web.back;

import static com.course.core.constant.Constants.CREATE;
import static com.course.core.constant.Constants.DELETE_SUCCESS;
import static com.course.core.constant.Constants.EDIT;
import static com.course.core.constant.Constants.MESSAGE;
import static com.course.core.constant.Constants.OPRT;
import static com.course.core.constant.Constants.SAVE_SUCCESS;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.course.common.orm.RowSide;
import com.course.common.web.Servlets;
import com.course.core.constant.Constants;
import com.course.core.domain.Role;
import com.course.core.domain.Site;
import com.course.core.domain.User;
import com.course.core.domain.UserRole;
import com.course.core.holder.MenuHolder;
import com.course.core.service.OperationLogService;
import com.course.core.service.RoleService;
import com.course.core.service.UserRoleService;
import com.course.core.service.UserService;
import com.course.core.support.Backends;
import com.course.core.support.Context;
import com.course.core.support.Response;

/**
 * RoleController
 * 
 * @author benfang
 * 
 */
@Controller
@RequestMapping("/core/role")
public class RoleController {
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);

	@RequiresPermissions("core:role:list")
	@RequestMapping("list.do")
	public String list(@PageableDefault(sort = { "seq", "id" }, direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		List<Role> list = service.findList(siteId, params, pageable.getSort());
		modelMap.addAttribute("list", list);
		return "core/role/role_list";
	}

	@RequiresPermissions("core:role:create")
	@RequestMapping("create.do")
	public String create(Integer id, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		if (id != null) {
			Role bean = service.get(id);
			Backends.validateDataInSite(bean, site.getId());
			modelMap.addAttribute("bean", bean);
		}
		modelMap.addAttribute(OPRT, CREATE);
		return "core/role/role_form";
	}

	@RequiresPermissions("core:role:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer position,
			@PageableDefault(sort = { "seq", "id" }, direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Role bean = service.get(id);
		Backends.validateDataInSite(bean, siteId);
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		RowSide<Role> side = service.findSide(siteId, params, bean, position, pageable.getSort());
		modelMap.addAttribute("infoPerms", bean.getInfoPerms());
		modelMap.addAttribute("nodePerms", bean.getNodePerms());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/role/role_form";
	}

	@RequiresPermissions("core:role:save")
	@RequestMapping("save.do")
	public String save(Role bean, Integer[] infoPermIds, Integer[] nodePermIds, String redirect,
			HttpServletRequest request, RedirectAttributes ra, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {

		Response resp = new Response(request, response, modelMap);
		Integer siteId = Context.getCurrentSiteId();
		service.save(bean, infoPermIds, nodePermIds, siteId);
		logService.operation("opr.role.add", bean.getName(), null, bean.getId(), request);
		logger.info("save Role, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);

		return resp.post(1);
	}

	@RequiresPermissions("core:role:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Role bean, Integer[] infoPermIds, Integer[] nodePermIds,
			Integer position, String redirect, HttpServletRequest request, RedirectAttributes ra,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {

		Response resp = new Response(request, response, modelMap);

		Site site = Context.getCurrentSite();
		Backends.validateDataInSite(bean, site.getId());
		// 如果拥有所有权限，则清空权限字符串。
		if (bean.getAllPerm()) {
			bean.setPerms(null);
		}
		if (infoPermIds == null) {
			infoPermIds = new Integer[0];
		}
		if (nodePermIds == null) {
			nodePermIds = new Integer[0];
		}
		service.update(bean, infoPermIds, nodePermIds);
		logService.operation("opr.role.edit", bean.getName(), null, bean.getId(), request);

		logger.info("update Role, name={}.", bean.getName());

		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);

		return resp.post(1);
	}

	@RequiresPermissions("core:role:update")
	@RequestMapping("batch_update.do")
	public String batchUpdate(Integer[] id, String[] name, Integer[] rank, String[] description,
			HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(id, site.getId());
		List<Role> beans = service.batchUpdate(id, name, rank, description);
		for (Role bean : beans) {
			logService.operation("opr.role.batchEdit", bean.getName(), null, bean.getId(), request);
			logger.info("batch update Role. name={}", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("core:role:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		validateIds(ids, site.getId());
		Role[] beans = service.delete(ids);
		for (Role bean : beans) {
			logService.operation("opr.role.delete", bean.getName(), null, bean.getId(), request);
			logger.info("delete Role, name={}", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	@RequiresPermissions("core:role:assignuser")
	@RequestMapping("assignuser.do")
	public String assignUser(Integer id, HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Role bean = service.get(id);
		Backends.validateDataInSite(bean, siteId);
		modelMap.addAttribute("bean", bean);

		return "core/role/role_assign_user";
	}

	@RequiresPermissions("core:role:assignuser")
	@RequestMapping("assignuserlist.do")
	public String assignuserlist(
			@PageableDefault(sort = { "user.realName" }, direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {

		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);

		Page<UserRole> pagedList = this.userRoleService.findPage(params, pageable);
		modelMap.addAttribute("pagedList", pagedList);

		return "core/role/role_assign_user_list";
	}

	@RequiresPermissions("core:role:addassignuser")
	@RequestMapping("addassignuser.do")
	public String addassignuser(@ModelAttribute("bean") Role bean, Integer[] userids, Integer position, String redirect,
			HttpServletRequest request, RedirectAttributes ra, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {

		Response resp = new Response(request, response, modelMap);

		Integer siteId = Context.getCurrentSiteId();

		for (Integer userid : userids) {
			User user = this.userService.get(userid);
			List<UserRole> userRoles = user.getUserRoles();
			boolean hascheck = false;
			for (UserRole userRole : userRoles) {
				if (userRole.getRole().getId() == bean.getId()) {
					hascheck = true;
					break;
				}
			}
			if (!hascheck) {
				this.userRoleService.update(user, new Integer[] { bean.getId() }, siteId);
			}
		}
		return resp.post(1);
	}

	@RequiresPermissions("core:role:deleteassignuser")
	@RequestMapping("deleteassignuser.do")
	public String deleteAssignUser(@ModelAttribute("bean") Role bean, Integer[] userids, String redirect,
			HttpServletRequest request, RedirectAttributes ra, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {

		Response resp = new Response(request, response, modelMap);
		for (Integer userid : userids) {
			User user = this.userService.get(userid);

			this.userRoleService.delete(user, bean);
		}

		return resp.post(1);
	}

	@RequiresPermissions("core:role:setmoduleperms")
	@RequestMapping("setmoduleperms.do")
	public String setModulePerms(Integer id, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Integer siteId = Context.getCurrentSiteId();
		Role bean = service.get(id);
		Backends.validateDataInSite(bean, siteId);
		modelMap.addAttribute("bean", bean);

		modelMap.addAttribute("menus", menuHolder.getMenus());
		Servlets.setNoCacheHeader(response);

		return "core/role/role_set_module_perms";
	}

	@RequiresPermissions("core:role:savemoduleperms")
	@RequestMapping("savemoduleperms.do")
	public String savemoduleperms(@ModelAttribute("bean") Role bean, String redirect, HttpServletRequest request,
			RedirectAttributes ra, HttpServletResponse response, org.springframework.ui.Model modelMap) {

		Response resp = new Response(request, response, modelMap);

		bean.setAllPerm(false);

		this.service.update(bean, null, null);

		return resp.post(1);
	}


	
	
	@ModelAttribute("bean")
	public Role preloadBean(@RequestParam(required = false) Integer oid) {
		if (oid != null) {
			Role bean = service.get(oid);
			Role obj = new Role();
			BeanUtils.copyProperties(bean, obj);
			return obj;
		} else {
			return null;
		}
	}

	private void validateIds(Integer[] ids, Integer siteId) {
		for (Integer id : ids) {
			Backends.validateDataInSite(service.get(id), siteId);
		}
	}

	@Autowired
	private OperationLogService logService;
	@Autowired
	private RoleService service;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private UserService userService;
	@Autowired
	private MenuHolder menuHolder;
}
