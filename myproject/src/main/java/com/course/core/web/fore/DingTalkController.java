package com.course.core.web.fore;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.course.common.dingtalk.AuthHelper;
import com.course.common.dingtalk.UserHelper;
import com.course.common.security.CredentialsDigest;
import com.course.core.constant.Constants;
import com.course.core.domain.Site;
import com.course.core.domain.User;
import com.course.core.service.UserService;
import com.course.core.support.Context;
import com.course.core.support.ForeContext;
import com.course.core.support.Response;
import com.taobao.api.ApiException;

@Controller
public class DingTalkController {
	public static final String LOGIN_DINGTALK_TEMPLATE = "sys_member_dingtalk_login.html";

	@RequestMapping(value = { "/dingtalk/haslogin", Constants.SITE_PREFIX_PATH + "/dingtalk/haslogin" })
	public String dingtalkLoginRedirectUri(String code, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {

		Map<String, String> items = AuthHelper.getConfig(request);
		modelMap.addAllAttributes(items);

		modelMap.addAttribute("code", code);

		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);

		return site.getTemplate(LOGIN_DINGTALK_TEMPLATE);
	}

	@RequestMapping(value = { "/dingtalk", Constants.SITE_PREFIX_PATH + "/dingtalk" })
	public String dingtalkLogin(String fallbackUrl, HttpServletRequest request, org.springframework.ui.Model modelMap) {

		Map<String, String> items = AuthHelper.getConfig(request);
		modelMap.addAllAttributes(items);

		Site site = Context.getCurrentSite();
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);

		return site.getTemplate(LOGIN_DINGTALK_TEMPLATE);
	}

	// encoding参数使用utf-8
	public String urlEncode(String value, String encoding) {
		if (value == null) {
			return "";
		}

		try {
			String encoded = URLEncoder.encode(value, encoding);
			return encoded.replace("+", "%20").replace("*", "%2A").replace("~", "%7E").replace("/", "%2F");
		} catch (UnsupportedEncodingException e) {
			throw new IllegalArgumentException("FailedToEncodeUri", e);
		}
	}

	@RequestMapping(value = { "/dingtalk/login/{dinguserid:[0-9]+}",
			Constants.SITE_PREFIX_PATH + "/dingtalk/login/{dinguserid:[0-9]+}" })
	public String dingtalkDoLogin(@PathVariable String dinguserid, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {

		if (dinguserid == null)
			return "redirect:/login";

		User user = this.userService.findByDingUserId(dinguserid);

		// String rawPassword = this.credentialsDigest.digest("1",
		// user.getSaltBytes());
		// 注册成功后自动登录
		UsernamePasswordToken loginToken = new UsernamePasswordToken(user.getUsername(), user.getPassword());
		loginToken.setRememberMe(true);
		Subject currentUser = SecurityUtils.getSubject();
		currentUser.login(loginToken);

		// 登录时，session会失效，先将SavedRequest取出
		SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute(WebUtils.SAVED_REQUEST_KEY);
		// 将SavedRequest放回session
		request.getSession().setAttribute(WebUtils.SAVED_REQUEST_KEY, savedRequest);

		return "redirect:/my";
	}

	@RequestMapping(value = { "/dingtalk/binduser",
			Constants.SITE_PREFIX_PATH + "/dingtalk/binduser" }, method = RequestMethod.POST)
	public String bindUserinfo(String username, String dinguserid, String password, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) throws Exception {
		Response resp = new Response(request, response, modelMap);

		User user = this.userService.findByUsername(username);

		if (user == null) {
			return resp.post(0, "用户名输入有误");
		}

		if (!credentialsDigest.matches(user.getPassword(), password, user.getSaltBytes())) {
			return resp.post(0, "密码输入有误");
		}

		user.setDingUserId(dinguserid);

		this.userService.update(user, user.getDetail());

		return resp.post(1);
	}

	@RequestMapping(value = { "/dingtalk/userinfo",
			Constants.SITE_PREFIX_PATH + "/dingtalk/userinfo" }, method = RequestMethod.POST)
	public String userinfo(String code, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws Exception {
		Response resp = new Response(request, response, modelMap);

		String accessToken = AuthHelper.getAccessToken();

		String dingUserID = UserHelper.getUserInfo(accessToken, code).getUserid();

		User user = this.userService.findByDingUserId(dingUserID);

		if (user != null) {

			resp.addData("dingtalk", 1);
			resp.addData("dinguserid", user.getDingUserId());

		} else {

			String mobile = UserHelper.getUser(accessToken, dingUserID).getMobile();

			user = this.userService.findByMobile(mobile);

			if (user != null) {

				// 自动登录

				String password = user.getPassword() == null ? "" : user.getPassword();
				UsernamePasswordToken loginToken = new UsernamePasswordToken(user.getUsername(), password);
				loginToken.setRememberMe(true);
				Subject currentUser = SecurityUtils.getSubject();
				currentUser.login(loginToken);

				// 登录时，session会失效，先将SavedRequest取出
				SavedRequest savedRequest = (SavedRequest) request.getSession()
						.getAttribute(WebUtils.SAVED_REQUEST_KEY);
				// 将SavedRequest放回session
				request.getSession().setAttribute(WebUtils.SAVED_REQUEST_KEY, savedRequest);

				resp.addData("dingtalk", 2);
				resp.addData("dinguserid", dingUserID);
			} else {

				resp.addData("dingtalk", 0);
				resp.addData("dinguserid", dingUserID);
			}
		}

		return resp.post(1);
	}

	public static void main(String[] args) throws ApiException, IllegalStateException, UnsupportedEncodingException,
			InvalidKeyException, NoSuchAlgorithmException {
		// DefaultDingTalkClient client = new
		// DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
		// OapiGettokenRequest tokenrequest = new OapiGettokenRequest();
		// tokenrequest.setAppkey("ding4cw4wplgtnajqowk");
		// tokenrequest.setAppsecret("wQ5SLxQz6Ho89XZ6mOyNlm5yWPpE7TmPqbOvo3qK1pjtICke8Wve11x-pwu5YpRl");
		// tokenrequest.setHttpMethod("GET");
		// OapiGettokenResponse response = client.execute(tokenrequest);

	}

	@Autowired
	private CredentialsDigest credentialsDigest;

	@Autowired
	private UserService userService;

}
