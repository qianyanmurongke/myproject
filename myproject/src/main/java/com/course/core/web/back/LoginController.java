package com.course.core.web.back;

import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_ERROR_KEY_ATTRIBUTE_NAME;
import static org.apache.shiro.web.filter.authc.FormAuthenticationFilter.DEFAULT_USERNAME_PARAM;

import java.util.Date;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.course.common.captcha.Captchas;
import com.course.common.web.Validations;
import com.course.core.constant.Constants;
import com.course.core.domain.User;
import com.course.core.domain.UserDetail;
import com.course.core.service.UserService;
import com.course.core.support.Response;
import com.octo.captcha.service.CaptchaService;

/**
 * LoginController
 * 
 * @author benfang
 * 
 */
@Controller
public class LoginController {

	@RequestMapping(value = "/login.do")
	public String login() {
		return "login";
		// return "login_wz22";
	}

	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public String fail(@RequestParam(DEFAULT_USERNAME_PARAM) String username, HttpServletRequest request,
			RedirectAttributes redirect) {
		Object errorName = request.getAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME);
		if (errorName != null) {
			redirect.addFlashAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, errorName);
		}
		redirect.addFlashAttribute("username", username);
		return "redirect:login.do";
	}

	// @RequestMapping(value = "/login.do")
	// public String login(String username, String password, HttpServletRequest
	// request, RedirectAttributes redirect) {
	// String result = "redirect:index.do";
	// if (StringUtils.isBlank(username)) {
	// return "login";
	// }
	// User user = userService.findByUsername(username);
	// String message = null;
	// if (user != null) {
	// if (credentialsDigest.matches(user.getPassword(), password,
	// user.getSaltBytes())) {
	//
	// String rawPassword = this.credentialsDigest.digest(password,
	// user.getSaltBytes());
	//
	// // 注册成功后自动登录
	// UsernamePasswordToken loginToken = new
	// UsernamePasswordToken(user.getUsername(), rawPassword);
	// loginToken.setRememberMe(true);
	// Subject currentUser = SecurityUtils.getSubject();
	// currentUser.login(loginToken);
	//
	// // 登录时，session会失效，先将SavedRequest取出
	// SavedRequest savedRequest = (SavedRequest) request.getSession()
	// .getAttribute(WebUtils.SAVED_REQUEST_KEY);
	// // 将SavedRequest放回session
	// request.getSession().setAttribute(WebUtils.SAVED_REQUEST_KEY,
	// savedRequest);
	//
	// // 添加登录日志
	// String userAgent = request.getHeader("user-agent");
	// String ip = Servlets.getRemoteAddr(request);
	// this.operationLogService.loginSuccess(ip, user.getId(), userAgent);
	//
	// this.userShiroService.updateLoginSuccess(user.getId(), ip);
	//
	// } else {
	// message = "org.apache.shiro.authc.IncorrectCredentialsException'";
	// }
	// } else {
	// message = "用户名不存在";
	// }
	// if (StringUtils.isNotBlank(message)) {
	// redirect.addFlashAttribute(DEFAULT_ERROR_KEY_ATTRIBUTE_NAME, message);
	// }
	//
	// redirect.addFlashAttribute("username", username);
	//
	// return result;
	// }

	@RequestMapping(value = "/forgotpassword/forgot_password.do")
	public String forgotPassword(HttpServletRequest request, org.springframework.ui.Model modelMap) {

		return "forgot_password";
	}

	@RequestMapping(value = "/forgotpassword/sendforgotpasswordmobile.do")
	public String SendForgotPasswordMobile(String mobile, String captcha, HttpServletRequest request,
			RedirectAttributes ra, HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);

		if (!Validations.notEmpty(mobile)) {
			return resp.post(1, "您的手机号码不能为空！");
		}
		if (!Captchas.isValid(captchaService, request, captcha)) {
			return resp.post(0, "验证码输入有误！");
		}
		User forgotUser = userService.findByMobile(mobile);
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

	@RequestMapping(value = "/forgotpassword/forgot_password_submit.do", method = RequestMethod.POST)
	public String forgotPasswordSubmit(String mobile, String captcha, String verificationCode,
			HttpServletRequest request, RedirectAttributes ra, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);

		if (!Validations.notEmpty(mobile)) {
			return resp.post(0, "您的手机号码不能为空！");
		}
		if (!Validations.notEmpty(verificationCode)) {
			return resp.post(0, "短信验证码不能为空！");
		}
		User forgotUser = userService.findByMobile(mobile);
		if (forgotUser == null) {
			return resp.post(0, "短信验证码不正确或已失效");
		}
		if (!forgotUser.getValidationKey().equals(verificationCode)) {
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

	@RequestMapping(value = "/forgotpassword/retrieve_password.do")
	public String retrievePasswordForm(HttpServletRequest request, org.springframework.ui.Model modelMap) {

		HttpSession session = request.getSession();
		if (session.getAttribute("retrieve_password_key") == null) {
			return "redirect:forgot_password.do";
		}
		String key = session.getAttribute("retrieve_password_key").toString();
		User forgotUser = userService.findByValidation(Constants.RETRIEVE_PASSWORD_TYPE, key);
		if (forgotUser == null) {
			return "redirect:forgot_password.do";
		}
		// 找不到用户、验证时间为空或者超过5分钟，则验证失效。
		if (forgotUser.getValidationDate() == null
				|| System.currentTimeMillis() - forgotUser.getValidationDate().getTime() > 5 * 60 * 1000) {
			return "redirect:forgot_password.do";
		}
		modelMap.addAttribute("forgotUser", forgotUser);
		modelMap.addAttribute("key", key);

		return "retrieve_password";
	}

	@RequestMapping(value = "/forgotpassword/retrieve_password_submit.do", method = RequestMethod.POST)
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

	@Autowired
	private CaptchaService captchaService;

	@Autowired
	private UserService userService;


}
