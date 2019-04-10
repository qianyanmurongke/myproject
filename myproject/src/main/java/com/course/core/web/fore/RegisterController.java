package com.course.core.web.fore;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.course.common.captcha.Captchas;
import com.course.common.web.Servlets;
import com.course.common.web.Validations;
import com.course.core.constant.Constants;
import com.course.core.domain.GlobalMail;
import com.course.core.domain.GlobalRegister;
import com.course.core.domain.Site;
import com.course.core.domain.User;
import com.course.core.domain.UserDetail;
import com.course.core.service.MemberGroupService;
import com.course.core.service.OrgService;
import com.course.core.service.UserService;
import com.course.core.support.Context;
import com.course.core.support.ForeContext;
import com.course.core.support.Response;
import com.octo.captcha.service.CaptchaService;

/**
 * RegisterController
 * 
 * @author benfang
 * 
 */
@Controller
public class RegisterController {
	/**
	 * 注册模板
	 */
	public static final String REGISTER_TEMPLATE = "sys_member_register.html";
	/**
	 * 注册结果模板。提示会员注册成功，或提示会员接收验证邮件。
	 */
	public static final String REGISTER_MESSAGE_TEMPLATE = "sys_member_register_message.html";
	/**
	 * 验证会员模板
	 */
	public static final String VERIFY_MEMBER_TEMPLATE = "sys_member_verify_member.html";
	/**
	 * 忘记密码模板
	 */
	public static final String FORGOT_PASSWORD_TEMPLATE = "sys_member_forgot_password.html";
	/**
	 * 找回密码模板
	 */
	public static final String RETRIEVE_PASSWORD_TEMPLATE = "sys_member_retrieve_password.html";

	@RequestMapping(value = { "/register", Constants.SITE_PREFIX_PATH + "/register" })
	public String registerForm(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		Site site = Context.getCurrentSite();
		GlobalRegister registerConf = site.getGlobal().getRegister();
		if (registerConf.getMode() == GlobalRegister.MODE_OFF) {
			return resp.warning("register.off");
		}
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(REGISTER_TEMPLATE);
	}

	@RequestMapping(value = { "/register", Constants.SITE_PREFIX_PATH + "/register" }, method = RequestMethod.POST)
	public String registerSubmit(String captcha, String username, String password, String email, String gender,
			Date birthDate, String bio, String comeFrom, String qq, String msn, String weixin,
			HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		Site site = Context.getCurrentSite();
		GlobalRegister reg = site.getGlobal().getRegister();
		String result = validateRegisterSubmit(request, resp, reg, captcha, username, password, email, gender);
		if (resp.hasErrors()) {
			return result;
		}

		int verifyMode = reg.getVerifyMode();
		String ip = Servlets.getRemoteAddr(request);
		int groupId = reg.getGroupId();
		int orgId = reg.getOrgId();
		int status = verifyMode == GlobalRegister.VERIFY_MODE_NONE ? User.NORMAL : User.UNACTIVATED;
		User user = userService.register(ip, groupId, orgId, status, username, password, email, null, null, null,
				gender, birthDate, bio, comeFrom, qq, msn, weixin);
		if (verifyMode == GlobalRegister.VERIFY_MODE_EMAIL) {
			GlobalMail mail = site.getGlobal().getMail();
			String subject = reg.getVerifyEmailSubject();
			String text = reg.getVerifyEmailText();
			userService.sendVerifyEmail(site, user, mail, subject, text);
		}
		resp.addData("verifyMode", verifyMode);
		resp.addData("id", user.getId());
		resp.addData("username", user.getUsername());
		resp.addData("email", user.getEmail());
		return resp.post();
	}

	@RequestMapping(value = { "/register_message", Constants.SITE_PREFIX_PATH + "/register_message" })
	public String registerMessage(String email, Integer verifyMode, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		Site site = Context.getCurrentSite();
		GlobalRegister reg = site.getGlobal().getRegister();
		String username = Servlets.getParam(request, "username");
		String result = validateRegisterMessage(request, resp, reg, username, email, verifyMode);
		if (resp.hasErrors()) {
			return result;
		}

		User registerUser = userService.findByUsername(username);
		modelMap.addAttribute("registerUser", registerUser);
		modelMap.addAttribute("verifyMode", verifyMode);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(REGISTER_MESSAGE_TEMPLATE);
	}

	@RequestMapping(value = { "/verify_member", Constants.SITE_PREFIX_PATH + "/verify_member" })
	public String verifyMember(String key, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		List<String> messages = resp.getMessages();
		Site site = Context.getCurrentSite();
		if (!Validations.notEmpty(key, messages, "key")) {
			return resp.badRequest();
		}
		User keyUser = userService.findByValidation(Constants.VERIFY_MEMBER_TYPE, key);
		userService.verifyMember(keyUser);
		modelMap.addAttribute("keyUser", keyUser);
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(VERIFY_MEMBER_TEMPLATE);
	}

	@RequestMapping(value = { "/forgot_password", Constants.SITE_PREFIX_PATH + "/forgot_password" })
	public String forgotPasswordForm(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(FORGOT_PASSWORD_TEMPLATE);
	}

	@RequestMapping(value = { "/forgot_password/sendforgotpasswordmobile",
			Constants.SITE_PREFIX_PATH + "/forgot_password/sendforgotpasswordmobile" }, method = RequestMethod.POST)
	public String sendForgotPasswordMobile(String username, String captcha, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		if (!Validations.notEmpty(username)) {
			return resp.post(1, "您的手机号码不能为空！");
		}
		if (!Captchas.isValid(captchaService, request, captcha)) {
			return resp.post(0, "验证码输入有误！");
		}
		User forgotUser = userService.findByMobile(username);
		if (forgotUser == null) {
			return resp.post(1, "当前手机号码在系统没有注册");
		}

		int max = 999999;
		int min = 100000;
		Random random = new Random();

		String paramCaptcha = Integer.toString(random.nextInt(max) % (max - min + 1) + min);

		System.out.print("短信验证码：" + paramCaptcha);

		UserDetail detail = forgotUser.getDetail();
		forgotUser.setValidationKey(paramCaptcha);
		forgotUser.setValidationType(Constants.RETRIEVE_PASSWORD_TYPE);
		detail.setValidationDate(new Date());

		this.userService.update(forgotUser, detail);

		return resp.post(1);
	}

	@RequestMapping(value = { "/forgot_password",
			Constants.SITE_PREFIX_PATH + "/forgot_password" }, method = RequestMethod.POST)
	public String forgotPasswordSubmit(String username, String captcha, String smscaptcha, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);

		if (!Validations.notEmpty(username)) {
			return resp.post(0, "您的手机号码不能为空！");
		}
		if (!Validations.notEmpty(smscaptcha)) {
			return resp.post(0, "短信验证码不能为空！");
		}
		User forgotUser = userService.findByMobile(username);
		if (forgotUser == null) {
			return resp.post(0, "短信验证码不正确或已失效");
		}
		if (!forgotUser.getValidationKey().equals(smscaptcha)) {
			return resp.post(0, "短信验证码不正确或已失效");
		}
		// 找不到用户、验证时间为空或者超过2分钟，则验证失效。
		if (forgotUser.getValidationDate() == null
				|| System.currentTimeMillis() - forgotUser.getValidationDate().getTime() > 2 * 60 * 1000) {
			return resp.post(0, "短信验证码不正确或已失效");
		}

		String key = StringUtils.remove(UUID.randomUUID().toString(), '-');

		forgotUser.setValidationKey(key);
		forgotUser.setValidationType(Constants.RETRIEVE_PASSWORD_TYPE);
		UserDetail detail = forgotUser.getDetail();
		detail.setValidationDate(new Date());

		this.userService.update(forgotUser, detail);

		HttpSession session = request.getSession();
		session.setAttribute("retrieve_password_key", key);

		return resp.post(1);
	}

	@RequestMapping(value = { "/retrieve_password", Constants.SITE_PREFIX_PATH + "/retrieve_password" })
	public String retrievePasswordForm(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		HttpSession session = request.getSession();
		if (session.getAttribute("retrieve_password_key") == null) {
			return "redirect:forgot_password";
		}
		String key = session.getAttribute("retrieve_password_key").toString();
		User forgotUser = userService.findByValidation(Constants.RETRIEVE_PASSWORD_TYPE, key);
		if (forgotUser == null) {
			return "forgot_password";
		}
		// 找不到用户、验证时间为空或者超过5分钟，则验证失效。
		if (forgotUser.getValidationDate() == null
				|| System.currentTimeMillis() - forgotUser.getValidationDate().getTime() > 5 * 60 * 1000) {
			return "redirect:forgot_password";
		}

		modelMap.addAttribute("forgotUser", forgotUser);
		modelMap.addAttribute("key", key);
		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(RETRIEVE_PASSWORD_TEMPLATE);
	}

	@RequestMapping(value = { "/retrieve_password",
			Constants.SITE_PREFIX_PATH + "/retrieve_password" }, method = RequestMethod.POST)
	public String retrievePasswordSubmit(String key, String password, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		if (!Validations.notEmpty(key)) {
			return resp.post(401, "修改密码已失效");
		}

		if (!Validations.notEmpty(password)) {
			return resp.post(0, "密码不能为空");
		}

		User forgotUser = userService.findByValidation(Constants.RETRIEVE_PASSWORD_TYPE, key);
		if (forgotUser == null) {
			return resp.post(401, "修改密码已失效");
		}
		// 找不到用户、验证时间为空或者超过1分钟，则验证失效。
		if (forgotUser.getValidationDate() == null
				|| System.currentTimeMillis() - forgotUser.getValidationDate().getTime() > 5 * 60 * 1000) {
			return resp.post(401, "修改密码已失效");
		}

		userService.passwordChange(forgotUser, password);
		return resp.post(1);
	}

	@ResponseBody
	@RequestMapping(value = { "/check_username", Constants.SITE_PREFIX_PATH + "/check_username" })
	public String checkUsername(String username, String original, HttpServletRequest request,
			HttpServletResponse response) {
		Servlets.setNoCacheHeader(response);
		if (StringUtils.isBlank(username)) {
			return "true";
		}
		if (StringUtils.equals(username, original)) {
			return "true";
		}
		// 检查数据库是否重名
		boolean exist = userService.usernameExist(username);
		if (!exist) {
			return "true";
		} else {
			return "false";
		}
	}

	private String validateRegisterSubmit(HttpServletRequest request, Response resp, GlobalRegister reg, String captcha,
			String username, String password, String email, String gender) {
		List<String> messages = resp.getMessages();
		if (!Captchas.isValid(captchaService, request, captcha)) {
			return resp.post(100, "error.captcha");
		}
		if (reg.getMode() == GlobalRegister.MODE_OFF) {
			return resp.post(501, "register.off");
		}
		Integer groupId = reg.getGroupId();
		if (groupService.get(groupId) == null) {
			return resp.post(502, "register.groupNotSet");
		}
		Integer orgId = reg.getOrgId();
		if (orgService.get(orgId) == null) {
			return resp.post(503, "register.orgNotSet");
		}

		if (!Validations.notEmpty(username, messages, "username")) {
			return resp.post(401);
		}
		if (!Validations.length(username, reg.getMinLength(), reg.getMaxLength(), messages, "username")) {
			return resp.post(402);
		}
		if (!Validations.pattern(username, reg.getValidCharacter(), messages, "username")) {
			return resp.post(403);
		}
		if (!Validations.notEmpty(password, messages, "password")) {
			return resp.post(404);
		}
		if (reg.getVerifyMode() == GlobalRegister.VERIFY_MODE_EMAIL
				&& !Validations.notEmpty(email, messages, "email")) {
			return resp.post(405);
		}
		if (!Validations.email(email, messages, "email")) {
			return resp.post(406);
		}
		if (!Validations.pattern(gender, "[F,M]", messages, "gender")) {
			return resp.post(407);
		}
		return null;
	}

	private String validateRegisterMessage(HttpServletRequest request, Response resp, GlobalRegister reg,
			String username, String email, Integer verifyMode) {
		List<String> messages = resp.getMessages();
		if (!Validations.notEmpty(username, messages, "username")) {
			return resp.badRequest();
		}
		if (!Validations.notEmpty(email, messages, "email")) {
			return resp.badRequest();
		}
		if (!Validations.notNull(verifyMode, messages, "verifyMode")) {
			return resp.badRequest();
		}
		User registerUser = userService.findByUsername(username);
		if (!Validations.exist(registerUser)) {
			return resp.notFound();
		}
		if (!registerUser.getEmail().equals(email)) {
			return resp.notFound("email not found: " + email);
		}
		if (reg.getMode() == GlobalRegister.MODE_OFF) {
			return resp.warning("register.off");
		}
		return null;
	}

	@Autowired
	private CaptchaService captchaService;
	@Autowired
	private MemberGroupService groupService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private UserService userService;
}
