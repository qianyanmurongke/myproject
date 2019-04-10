package com.course.core.web.fore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.course.common.dingtalk.AuthHelper;
import com.course.common.dingtalk.UserHelper;
import com.course.core.support.Response;
import com.dingtalk.open.client.api.model.corp.CorpUserDetail;

/**
 * 钉钉免密登录 创建人：chen-chen 创建时间：2019年1月23日下午5:19:29 描述：
 * 
 * 
 * 版本号：1.0
 */
@Controller
public class DingLoginController {
	private static final Logger logger = LoggerFactory.getLogger(DingLoginController.class);

	/**
	 * 钉钉用户登录，显示当前登录用户的userId和名称
	 * 
	 * @param authCode
	 *            免登临时code
	 * @param request
	 * @param ra
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/ding_login", method = RequestMethod.POST)
	public String ding_login(String authCode, HttpServletRequest request, RedirectAttributes ra,
			HttpServletResponse response, org.springframework.ui.Model modelMap) throws Exception {
		Response resp = new Response(request, response, modelMap);

		// // 1.获取accessToken,注意正是代码要有异常流处理
		// String accessToken;
		// try {
		// DefaultDingTalkClient client = new
		// DefaultDingTalkClient("https://oapi.dingtalk.com/gettoken");
		// OapiGettokenRequest gettokenRequest = new OapiGettokenRequest();
		// gettokenRequest.setAppkey("ding5mjdevyk2z7ew8gg");
		// gettokenRequest.setAppsecret("bRl195qZTVlz0KS1ETQBg_r5VnAcmzbwPPdNuHHhButRJlpfbG76D6LoQrGj8ulf");
		// gettokenRequest.setHttpMethod("GET");
		// OapiGettokenResponse gettokenResponse =
		// client.execute(gettokenRequest);
		// accessToken = gettokenResponse.getAccessToken();
		// } catch (ApiException e) {
		// logger.error("getAccessToken failed", e);
		// throw new RuntimeException();
		// }
		//
		// // 2.获取用户信息。通过authCode和access_token获取免登用户的userid
		// DingTalkClient client = new
		// DefaultDingTalkClient("https://oapi.dingtalk.com/user/getuserinfo");
		// OapiUserGetuserinfoRequest getuserinfoRequest = new
		// OapiUserGetuserinfoRequest();
		// getuserinfoRequest.setCode(authCode);
		// getuserinfoRequest.setHttpMethod("GET");
		// OapiUserGetuserinfoResponse getuserinfoResponse;
		// try {
		// getuserinfoResponse = client.execute(getuserinfoRequest,
		// accessToken);
		// } catch (ApiException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return resp.post(0);
		// }
		// // 查询得到当前用户的userId
		// //
		// 获得到userId之后应用应该处理应用自身的登录会话管理（session）,避免后续的业务交互（前端到应用服务端）每次都要重新获取用户身份，提升用户体验
		// String userId = getuserinfoResponse.getUserid();
		//
		// // 3.获取用户详情。通过access_token和userid获取免登用户的信息
		// client = new
		// DefaultDingTalkClient("https://oapi.dingtalk.com/user/get");
		// OapiUserGetRequest getRequest = new OapiUserGetRequest();
		// getRequest.setUserid(userId);
		// getRequest.setHttpMethod("GET");
		// OapiUserGetResponse getResponse;
		// try {
		// getResponse = client.execute(getRequest, accessToken);
		// } catch (ApiException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// return resp.post(0);
		// }
		// resp.addData("mobile", getResponse.getMobile());
		// resp.addData("name", getResponse.getName());

		String accessToken = AuthHelper.getAccessToken();

		System.out.println("authCode=" + authCode + ";accessToken=" + accessToken);

		String dingUserID = UserHelper.getUserInfo(accessToken, authCode).getUserid();
		
		System.out.println("dingUserID=" + dingUserID);
		
		CorpUserDetail corpUserDetail = UserHelper.getUser(accessToken, dingUserID);

		String mobile = corpUserDetail.getMobile();
		String name = corpUserDetail.getName();

		resp.addData("mobile", mobile);
		resp.addData("name", name);

		return resp.post(1);
	}
}
