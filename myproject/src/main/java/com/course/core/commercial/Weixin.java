package com.course.core.commercial;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.mp.WeixinProxy;
import com.course.common.web.PathResolver;
import com.course.core.service.InfoQueryService;
import com.course.core.service.OperationLogService;

/**
 * 微信群发。
 * 
 * @author benfang
 *
 */
public class Weixin {

	public static String massWeixinForm(Integer[] ids, Integer queryNodeId, Integer queryNodeType,
			Integer queryInfoPermType, String queryStatus, HttpServletRequest request,
			org.springframework.ui.Model modelMap, WeixinProxy weixinProxy, InfoQueryService query)
			throws WeixinException {
		return "redirect:/support_genuine.jsp";
	}

	public static void massWeixin(String mode, Integer groupId, String towxname, String[] title, String[] author,
			String[] contentSourceUrl, String[] digest, Boolean[] showConverPic, String[] thumb,
			HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap,
			WeixinProxy weixinProxy, OperationLogService logService, PathResolver pathResolver) throws IOException {
		
	}
}
