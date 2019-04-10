package com.course.core.web.back;

import static com.course.core.constant.Constants.DELETE_SUCCESS;
import static com.course.core.constant.Constants.MESSAGE;
import static com.course.core.constant.Constants.OPERATION_FAILURE;
import static com.course.core.constant.Constants.OPERATION_SUCCESS;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import com.course.common.orm.RowSide;
import com.course.common.security.CredentialsDigest;
import com.course.common.web.Servlets;
import com.course.core.constant.Constants;
import com.course.core.domain.MailInbox;
import com.course.core.domain.OperationLog;
import com.course.core.domain.Org;
import com.course.core.domain.Site;
import com.course.core.domain.User;
import com.course.core.service.MailInboxService;
import com.course.core.service.OperationLogService;
import com.course.core.service.UserService;
import com.course.core.support.CmsException;
import com.course.core.support.Context;

/**
 * HomepageController
 * 
 * @author benfang
 * 
 * 
 */
@Controller
@RequestMapping("/core/homepage")
public class HomepageController {
	private static final Logger logger = LoggerFactory.getLogger(HomepageController.class);

	@RequestMapping("welcome.do")
	public String welcome(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		User user = Context.getCurrentUser();
		modelMap.addAttribute("site", site);
		modelMap.addAttribute("user", user);

		return "core/homepage/welcome";
	}

	@RequiresPermissions("core:homepage:environment")
	@RequestMapping("environment.do")
	public String environment(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Properties props = System.getProperties();
		Runtime runtime = Runtime.getRuntime();
		long freeMemory = runtime.freeMemory();
		long totalMemory = runtime.totalMemory();
		long maxMemory = runtime.maxMemory();
		long usedMemory = totalMemory - freeMemory;
		long useableMemory = maxMemory - totalMemory + freeMemory;
		int div = 1000;
		double freeMemoryMB = ((double) freeMemory) / div / div;
		double totalMemoryMB = ((double) totalMemory) / div / div;
		double usedMemoryMB = ((double) usedMemory) / div / div;
		double maxMemoryMB = ((double) maxMemory) / div / div;
		double useableMemoryMB = ((double) useableMemory) / div / div;
		modelMap.addAttribute("props", props);
		modelMap.addAttribute("maxMemoryMB", maxMemoryMB);
		modelMap.addAttribute("usedMemoryMB", usedMemoryMB);
		modelMap.addAttribute("useableMemoryMB", useableMemoryMB);
		modelMap.addAttribute("totalMemoryMB", totalMemoryMB);
		modelMap.addAttribute("freeMemoryMB", freeMemoryMB);
		return "core/homepage/environment";
	}

	/**
	 * 我的信息
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	
	@RequestMapping(value = "personal_view.do")
	public String personalView(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("type", "personal_view");
		Map<String, String[]> params = new LinkedHashMap<String, String[]>();
		params.put("EQ_Juser.id", new String[] { Context.getCurrentUserId().toString() });
		params.put("EQ_type", new String[] { "1" });
		Integer siteId = Context.getCurrentSiteId();
		Pageable pageable = new PageRequest(0, 5, new Sort(Direction.DESC, "time"));
		Page<OperationLog> pagedList = logService.findAll(siteId, params, pageable);
		modelMap.addAttribute("OperationLogList", pagedList.getContent());

		params = new LinkedHashMap<String, String[]>();
		params.put("EQ_Juser.id", new String[] { Context.getCurrentUserId().toString() });
		params.put("EQ_type", new String[] { "2" });
		pageable = new PageRequest(0, 6, new Sort(Direction.DESC, "time"));
		Page<OperationLog> pagedLoginLogList = logService.findAll(siteId, params, pageable);
		modelMap.addAttribute("LoginLogList", pagedLoginLogList.getContent());

		return "core/homepage/personal_view";
	}

	/**
	 * 头像设置
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	// @RequiresPermissions("core:homepage:personal:avatar")
	@RequestMapping(value = "personal_avatar.do")
	public String personalAvatar(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("type", "personal_avatar");
		return "core/homepage/personal_avatar";
	}



	@ModelAttribute
	public void preloadBean(@RequestParam(required = false) Integer oid, org.springframework.ui.Model modelMap) {
		if (oid != null) {
			User bean = userService.get(oid);
			if (bean != null) {
				User currUser = Context.getCurrentUser();
				if (currUser.getRank() > bean.getRank()) {
					throw new CmsException("error.forbiddenData");
				}
				Site site = Context.getCurrentSite();
				String orgTreeNumber = site.getOrg().getTreeNumber();
				Org org = bean.getOrg();
				if (!org.getTreeNumber().startsWith(orgTreeNumber)) {
					throw new CmsException("error.forbiddenData");
				}
				User obj = new User();
				BeanUtils.copyProperties(bean, obj);
				modelMap.addAttribute("bean", obj);
				modelMap.addAttribute("detail", obj.getDetail());
				
			}
		}
	}

	/**
	 * 安全设置
	 * 
	 * @param request
	 * @param modelMap
	 * @return
	 */
	// @RequiresPermissions("core:homepage:password:edit")
	@RequestMapping(value = "password_edit.do")
	public String ppasswordEdit(HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		modelMap.addAttribute("user", user);
		modelMap.addAttribute("type", "password_edit");
		return "core/homepage/personal_password";
	}

	// @RequiresPermissions("core:homepage:password:update")
	@RequestMapping(value = "passwrod_update.do")
	public String passwrodUpdate(String origPassword, String rawPassword, HttpServletRequest request,
			RedirectAttributes ra) {
		User user = Context.getCurrentUser();
		if (credentialsDigest.matches(user.getPassword(), origPassword, user.getSaltBytes())) {
			userService.updatePassword(user.getId(), rawPassword);

			Integer siteId = Context.getCurrentSiteId();
			Integer userId = Context.getCurrentUserId();
			String ip = Servlets.getRemoteAddr(request);
			String userAgent = request.getHeader("user-agent");
			logService.operation("opr.personal.password.edit", user.getUsername(), null, user.getId(), ip, userId,
					siteId, userAgent);
			logger.info("personal password edit, name={}.", user.getUsername());

			ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		} else {
			ra.addFlashAttribute(MESSAGE, OPERATION_FAILURE);
		}
		return "redirect:password_edit.do";
	}


	/**
	 * 检查接收人
	 */
	@RequestMapping(value = "check_receiver.do")
	@ResponseBody
	public String checkReceiver(String receiverUsername) {
		return String.valueOf(doCheckReceiver(receiverUsername));
	}

	@RequiresPermissions("core:homepage:mail_inbox:list")
	@RequestMapping(value = "mail_inbox_list.do")
	public String mailInboxList(@RequestParam(defaultValue = "false") boolean unread,
			@PageableDefault(sort = "receiveTime", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		Page<MailInbox> pagedList = inboxService.findAll(user.getId(), params, pageable);
		modelMap.addAttribute("pagedList", pagedList);
		return "core/homepage/mail_inbox_list";
	}

	@RequiresPermissions("core:homepage:mail_inbox:show")
	@RequestMapping(value = "mail_inbox_show.do")
	public String mailInboxShow(Integer id, Integer position,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		User user = Context.getCurrentUser();
		MailInbox bean = inboxService.get(id);
		if (bean == null) {
			throw new CmsException("objectNotFound", MailInbox.class.getName(), String.valueOf(id));
		}
		if (!user.getId().equals(bean.getReceiver().getId())) {
			throw new CmsException("accessDenied");
		}
		inboxService.read(id);
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		RowSide<MailInbox> side = inboxService.findSide(user.getId(), params, bean, position, pageable.getSort());
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		return "core/homepage/mail_inbox_show";
	}

	@RequiresPermissions("core:homepage:mail_inbox:delete")
	@RequestMapping(value = "mail_inbox_delete.do")
	public String mailInboxDelete(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		User user = Context.getCurrentUser();
		validateMailInboxIds(ids, user.getId());
		inboxService.delete(ids);
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:mail_inbox_list.do";
	}

	/**
	 * 检查接收人是否合法
	 * 
	 * @param receiverUsername
	 *            接收人用户名
	 * @return
	 */
	private boolean doCheckReceiver(String receiverUsername) {
		User user = Context.getCurrentUser();
		// 用户名为空，或者与自己一样，则不能发送邮件
		if (StringUtils.isBlank(receiverUsername) || StringUtils.equals(receiverUsername, user.getUsername())) {
			return false;
		}
		User receiver = userService.findByUsername(receiverUsername);
		// 用户不存在
		if (receiver == null) {
			return false;
		}
		// 匿名用户不能接收站内信
		if (receiver.isAnonymous()) {
			return false;
		}
		return true;
	}



	private void validateMailInboxIds(Integer[] ids, Integer userId) {
		for (Integer id : ids) {
			MailInbox bean = inboxService.get(id);
			// 不是自己发送的不能删除
			if (!bean.getReceiver().getId().equals(userId)) {
				throw new CmsException("error.forbiddenData");
			}
		}
	}




	@Autowired
	private OperationLogService logService;
	@Autowired
	private MailInboxService inboxService;
	@Autowired
	private CredentialsDigest credentialsDigest;
	@Autowired
	private UserService userService;

}
