package com.course.core.web.fore;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.course.common.web.Servlets;
import com.course.core.constant.Constants;
import com.course.core.domain.User;
import com.course.core.service.OperationLogService;
import com.course.core.service.UserService;
import com.course.core.service.UserShiroService;
import com.course.core.support.Context;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.model.WeixinAccount;
import com.foxinmy.weixin4j.mp.api.OauthApi;
import com.foxinmy.weixin4j.mp.model.OauthToken;

@Controller
public class OAuthController {

	public static final String LOGIN_TEMPLATE = "sys_member_login.html";

	@RequestMapping(value = { "/my/oauth/bind/weixin", Constants.SITE_PREFIX_PATH + "/my/oauth/bind/weixin" })
	public String bindWeixin(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {

		OauthApi oauthApi = new OauthApi(new WeixinAccount(this.WeixinAppId, this.WeixinSecret));

		String takenUrl = oauthApi.getUserAuthorizationURL(this.WeixinRedirectUri, "STATE", this.WeixinScope);

		return "redirect:" + takenUrl;
	}

	@RequestMapping(value = { "/my/oauth/unbind/weixin", Constants.SITE_PREFIX_PATH + "/my/oauth/unbind/weixin" })
	public String unbindWeixin(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap, RedirectAttributes ra) {
		User user = Context.getCurrentUser();

		user.setWeixinOpenid(null);

		Map<String, String> customs = user.getCustoms();

		customs.put("weixin_nickname", "");

		user.setCustoms(customs);

		this.userService.update(user, user.getDetail());

		ra.addFlashAttribute("error_message", "微信账号解绑成功");

		return "redirect:/my/profile_set";
	}

	@RequestMapping(value = { "/my/oauth/authc/weixin", Constants.SITE_PREFIX_PATH + "/my/oauth/authc/weixin" })
	public String authcWeixin(String code, String state, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes ra) throws WeixinException {
		OauthApi oauthApi = new OauthApi(new WeixinAccount(this.WeixinAppId, this.WeixinSecret));

		OauthToken oauthToken = oauthApi.getAuthorizationToken(code);

		String ticket = oauthToken.getAccessToken();

		if (StringUtils.isBlank(ticket)) {
			ra.addFlashAttribute("error_message", "获取您的微信用户权限失败");
			return "redirect:/my/profile_set";
		}

		com.foxinmy.weixin4j.mp.model.User weixinUser = oauthApi.getAuthorizationUser(oauthToken);

		String openid = weixinUser.getOpenId();
		if (StringUtils.isBlank(openid)) {
			ra.addFlashAttribute("error_message", "获取您的微信用户权限失败");
			return "redirect:/my/profile_set";
		}

		User user = this.userShiroService.findByWeixinOpenid(openid);
		User currentUser = Context.getCurrentUser();
		if (user != null) {
			if (!currentUser.getId().equals(user.getId())) {
				ra.addFlashAttribute("error_message", "您的微信账号已绑定别的账号，请先解除绑定");
				return "redirect:/my/profile_set";
			}
		}

		currentUser.setWeixinOpenid(openid);

		Map<String, String> customs = currentUser.getCustoms();

		customs.put("weixin_nickname", weixinUser.getNickName());

		currentUser.setCustoms(customs);

		this.userService.update(currentUser, currentUser.getDetail());

		ra.addFlashAttribute("error_message", "您的微信账号绑定成功");
		return "redirect:/my/profile_set";
	}

	@RequestMapping(value = { "/oauth/login/weixin", Constants.SITE_PREFIX_PATH + "/oauth/login/weixin" })
	public String loginWeixin(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {

		OauthApi oauthApi = new OauthApi(new WeixinAccount(this.WeixinAppId, this.WeixinSecret));

		String takenUrl = oauthApi.getUserAuthorizationURL(this.WeixinRedirectUriLogin, "STATE", "snsapi_base");

		return "redirect:" + takenUrl;
	}

	@RequestMapping(value = { "/oauth/authc/weixin/login", Constants.SITE_PREFIX_PATH + "/oauth/authc/weixin/login" })
	public String authcWeixinLogin(String code, String state, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes ra) throws WeixinException {
		OauthApi oauthApi = new OauthApi(new WeixinAccount(this.WeixinAppId, this.WeixinSecret));

		OauthToken oauthToken = oauthApi.getAuthorizationToken(code);

		String ticket = oauthToken.getAccessToken();

		if (StringUtils.isBlank(ticket)) {
			ra.addFlashAttribute("shiroLoginFailure", "获取你的微信用户权限失败");
			return "redirect:/login";
		}

		String openid = oauthToken.getOpenId();
		if (StringUtils.isBlank(openid)) {
			ra.addFlashAttribute("shiroLoginFailure", "获取你的微信用户权限失败");
			return "redirect:/login";
		}

		User user = this.userShiroService.findByWeixinOpenid(openid);
		if (user != null) {

			// 注册成功后自动登录
			UsernamePasswordToken loginToken = new UsernamePasswordToken(user.getUsername(),
					(user.getPassword() == null ? "" : user.getPassword()));
			loginToken.setRememberMe(true);
			Subject currentUser = SecurityUtils.getSubject();
			currentUser.login(loginToken);

			// 登录时，session会失效，先将SavedRequest取出
			SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute(WebUtils.SAVED_REQUEST_KEY);
			// 将SavedRequest放回session
			request.getSession().setAttribute(WebUtils.SAVED_REQUEST_KEY, savedRequest);

			// 添加登录日志
			String userAgent = request.getHeader("user-agent");
			String ip = Servlets.getRemoteAddr(request);
			this.operationLogService.loginSuccess(ip, user.getId(), userAgent);
			return "redirect:/my";

		}
		ra.addFlashAttribute("shiroLoginFailure", "您的微信账号未绑定任何用户");
		return "redirect:/login";
	}

	@Value("${oauth.weixin.appid}")
	private String WeixinAppId;

	@Value("${oauth.weixin.secret}")
	private String WeixinSecret;

	@Value("${oauth.weixin.redirect_uri}")
	private String WeixinRedirectUri;

	@Value("${oauth.weixin.redirect_uri_login}")
	private String WeixinRedirectUriLogin;

	@Value("${oauth.weixin.response_type}")
	private String WeixinResponse_type;

	@Value("${oauth.weixin.scope}")
	private String WeixinScope;

	@Value("${oauth.weixin.authorize_url}")
	private String WeixinAuthorize_url;

	@Autowired
	private UserShiroService userShiroService;

	@Autowired
	private UserService userService;

	@Autowired
	private OperationLogService operationLogService;

}
