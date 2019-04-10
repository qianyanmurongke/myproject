package com.course.common.sendsms;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.course.common.util.HttpRequest;
import com.course.common.util.JsonMapper;

public class SendSMSClient {
	/**
	 * 短信验证接口
	 */
	public static final String POST_URL = "http://client.movek.net:8888/sms.aspx";

	@SuppressWarnings("unchecked")
	public static String SendSMS(String paramPhones, String paramContent) {
		paramPhones = "18067207665";
		// if (!StringUtils.isNotEmpty(paramPhones))
		// return "";
		//
		// String param =
		// "action=send&userid=993&account=SDK-A993-993&password=177066&content=【温州中学】"
		// + paramContent
		// + "&mobile=" + paramPhones + "&sendtime=";
		//
		// param = param + "&json=1";
		//
		// String resultJson = HttpRequest.sendGet(POST_URL, param);
		//
		// System.out.println(resultJson);
		//
		// Map<String, Object> items = (new JsonMapper()).fromJson(resultJson,
		// Map.class);
		//
		// if (!items.containsKey("code"))
		// return "";
		// if (items.get("code").toString().toLowerCase().equals("success"))
		// return ((Map<String, String>)
		// items.get("data")).get("taskID").toString();
		return "";
	}

	/**
	 * @return [{mobile=15355143315, taskid=111760140, content=Y,
	 *         receivetime=2018-09-20 14:04:18, extno=993}]
	 */
	@SuppressWarnings({ "unused", "unchecked" })
	public static List<Map<String, Object>> SyncReplySMS() {

		String param = "action=query&userid=993&account=SDK-A993-993&password=177066&json=1";

		String resultJson = HttpRequest.sendGet("http://client.movek.net:8888/callApi.aspx", param);
		//System.out.println(resultJson);
		Map<String, Object> items = (new JsonMapper()).fromJson(resultJson, Map.class);

		if (!items.containsKey("code"))
			return null;

		if (!items.get("code").toString().toLowerCase().equals("success"))
			return null;

		List<Map<String, Object>> list = (List<Map<String, Object>>) items.get("data");

		return list;
	}

	public static void main(String[] args) {
		// SendSMSClient.SendSMS("15355143315",
		// "您的孩子方芳与09月20日13:30请假因公司有事情是(Y)否(N)同意，请回复短信");
		SendSMSClient.SyncReplySMS();

	}
}
