package com.course.core.web.fore;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.course.common.ip.IPSeeker;
import com.course.common.orm.JpqlBuilder.Parameter;
import com.course.common.security.CredentialsDigest;
import com.course.common.security.Digests;
import com.course.common.util.CheckXml;
import com.course.common.util.Dates;
import com.course.common.util.Encodes;
import com.course.common.web.PathResolver;
import com.course.core.constant.Constants;
import com.course.core.domain.Site;
import com.course.core.domain.User;
import com.course.core.domain.UserDetail;
import com.course.core.service.OperationLogService;
import com.course.core.service.SQLService;
import com.course.core.service.UserService;
import com.course.core.support.Context;
import com.course.core.support.ForeContext;
import com.course.core.support.Response;
import com.eg.lockcloud.cxf.IEGLockCloudProxy;

/**
 * MemberController
 * 
 * @author benfang
 * 
 */
@Controller
public class MemberController {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public static final String MY_TEMPLATE = "my/sys_member_my.html";
	public static final String MY_JSON_TEMPLATE = "my/sys_member_my_json.html";
	public static final String PROFILE_TEMPLATE = "my/sys_member_profile.html";
	public static final String PASSWORD_TEMPLATE = "my/sys_member_password.html";
	public static final String ACCESSRECORD_TEMPLATE = "my/sys_member_accessrecord.html";
	public static final String ACCESSRECORD_JSON_TEMPLATE= "my/sys_member_accessrecord_json.html";

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = { "/my", Constants.SITE_PREFIX_PATH + "/my" })
	public String my(Integer page, Boolean isjson, Integer pagesize,String schoolplaceid,HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		User user = Context.getCurrentUser();
		if (user == null) {
			return resp.unauthorized();
		}
		
		Site site = Context.getCurrentSite();
		modelMap.addAttribute("own", true);
		modelMap.addAttribute("menutype", "my");
		Map<String, String[]> params = new HashMap<String, String[]>();	
		Sort defSort = new Sort(Direction.DESC, "pkid");
		Pageable pageable = new PageRequest((page == null ? 1 : page) - 1, (pagesize == null ? 15 : pagesize), defSort);
		if(schoolplaceid!=null){
			params.put("EQ_Jplace.id", new String[]{schoolplaceid});
		}
		if(!user.isAdmin()){
			String sql="select f_door_id from dr_access_user where f_user_id="+user.getId()+" union select f_door_id from dr_access_role "
					+ " left  join cms_user_role on  dr_access_role.f_role_id=cms_user_role.f_role_id where  cms_user_role.f_user_id="+user.getId()+"";
			List<Parameter> parameters=new ArrayList<Parameter>();
			List list=sqlService.list(sql, parameters, null);
			List<Integer> sectionids = new ArrayList<Integer>();
			if(list.size()>0){
				for (Object object : list) {
					Map row = (Map) object;
					sectionids.add(Integer.parseInt(row.get("f_door_id").toString()));
				}
			}
			String[] drids=new String[sectionids.size()];
			for(int i=0;i<sectionids.size();i++){
				drids[i]=sectionids.get(i).toString();
			}
			params.put("IN_id", drids);
		}
		
		
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		isjson = isjson == null ? false : isjson;
		if (isjson) {
			return site.getTemplate(MY_JSON_TEMPLATE);
		}
		return site.getTemplate(MY_TEMPLATE);
	}


	@RequestMapping(value = { "/my/account", Constants.SITE_PREFIX_PATH + "/my/account" }, method = RequestMethod.POST)
	public String accountSubmit(String gender, String mobile, String email, String cornet, String officePhone,
			String home_address, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		User user = Context.getCurrentUser();
		if (gender != null)
			user.setGender(gender);
		if (mobile != null)
			user.setMobile(mobile);
		if (email != null)
			user.setEmail(email);

		UserDetail detail = user.getDetail();
		userService.update(user, detail);

		return resp.post(0);
	}

	@RequestMapping(value = { "/my/profile", Constants.SITE_PREFIX_PATH + "/my/profile" })
	public String profileForm(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		
		modelMap.addAttribute("menutype", "profile");

		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);

		return site.getTemplate(PROFILE_TEMPLATE);
	}

	@RequestMapping(value = { "/my/profile", Constants.SITE_PREFIX_PATH + "/my/profile" }, method = RequestMethod.POST)
	public String profileSubmit(String gender, Date birthDate, String bio, String comeFrom, String qq, String msn,
			String weixin, HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		User user = Context.getCurrentUser();
		user.setGender(gender);
		user.setBirthDate(birthDate);
		UserDetail detail = user.getDetail();
		detail.setBio(bio);
		detail.setComeFrom(comeFrom);
		detail.setQq(qq);
		detail.setMsn(msn);
		detail.setQq(qq);
		userService.update(user, detail);
		return resp.post(0);
	}

	

	@RequestMapping(value = { "/my/password", Constants.SITE_PREFIX_PATH + "/my/password" })
	public String passwordForm(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		modelMap.addAttribute("menutype", "changepassword");
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		return site.getTemplate(PASSWORD_TEMPLATE);
	}

	@RequestMapping(value = { "/my/password",
			Constants.SITE_PREFIX_PATH + "/my/password" }, method = RequestMethod.POST)
	public String passwordSubmit(String password, String rawPassword, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		User user = Context.getCurrentUser();
		if (!credentialsDigest.matches(user.getPassword(), password, user.getSaltBytes())) {
			return resp.post(501, "member.passwordError");
		}
		if ("false".equals(allowMmemberChagePassword)) {
			return resp.post(502, "member.changePasswordForbidden");
		}
		userService.updatePassword(user.getId(), rawPassword);
		return resp.post();
	}

	/**
	 * 门禁记录
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 */
	@RequestMapping(value = { "/my/accessrecord", Constants.SITE_PREFIX_PATH + "/my/accessrecord" })
	public String accessrecordForm(Integer page, Boolean isjson, Integer pagesize,String doorName,HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		
		modelMap.addAttribute("menutype", "accessrecord");
		Map<String, String[]> params = new HashMap<String, String[]>();	
		Sort defSort = new Sort(Direction.DESC, "accessTime");
		if(doorName!=null){
			params.put("CONTAIN_Jdoor.doorName", new String[]{doorName});
		}
		Pageable pageable = new PageRequest((page == null ? 1 : page) - 1, (pagesize == null ? 15 : pagesize), defSort);
		User user=Context.getCurrentUser();
		if(!user.isAdmin()){
			params.put("EQ_Juser.id", new String[]{user.getId().toString()});
		}
		
		String sql="SELECT distinct date(f_access_time)  as datetime FROM door_controller_system.dr_access_record  order by f_access_time desc";
		List<Parameter> parameters = new ArrayList<Parameter>();
		@SuppressWarnings("rawtypes")
		List listdays = this.sqlService.list(sql, parameters, null);
		modelMap.addAttribute("listdays", listdays);
		modelMap.addAttribute("today", new Date());
		modelMap.addAttribute("yesterday", Dates.getYesterday());
		Map<String, Object> data = modelMap.asMap();
		ForeContext.setData(data, request);
		isjson = isjson == null ? false : isjson;
		if (isjson) {
			return site.getTemplate(ACCESSRECORD_JSON_TEMPLATE);
		}
		return site.getTemplate(ACCESSRECORD_TEMPLATE);
	}

	/**
	 * 远程开启
	 * @param status
	 * @param accessid
	 * @param lockaddr
	 * @param request
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws DocumentException
	 * @throws IOException 
	 */
	@RequestMapping(value = { "/my/copenandclose",
			Constants.SITE_PREFIX_PATH + "/my/copenandclose" }, method = RequestMethod.POST)
	@Transactional(propagation = Propagation.REQUIRED)
	public String copenandclose(Integer status,Integer accessid,String lockaddr, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) throws DocumentException, IOException {
		Response resp = new Response(request, response, modelMap);
		String pass = "";
		Document doc=null;
		Element rootElt=null;
		InputStream data;
		try {
			data = new ByteArrayInputStream("123456".getBytes("UTF-8"));
			pass = Encodes.encodeHex(Digests.md5(data)).toLowerCase();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		//远程开关机
		String xml="<?xml version='1.0' encoding='UTF-8'?>"+
				   "<eglockcloud name='RemoteOpenLock'>"+
				   "<pkgId>1</pkgId>"+
				   "<user>wzzx</user>"+
				   "<pass>"+pass+"</pass>"+
				   "<lockAddr>"+lockaddr+"</lockAddr>"+
				   "<openMode>"+status+"</openMode>"+
				   "</eglockcloud>";
		IEGLockCloudProxy  ieglockcloudproxy=new IEGLockCloudProxy();
		String resultsml=ieglockcloudproxy.transXml(xml);
		doc= DocumentHelper.parseText(resultsml);
		rootElt = doc.getRootElement();
		String openresult=rootElt.elementTextTrim("result");
		String taskId=rootElt.elementTextTrim("taskId");
		resp.addData("openresult", openresult);
		resp.addData("taskId", taskId);
		resp.addData("status", status);
		return resp.post(1);
	}
	
	@RequestMapping(value = { "/my/checktasksucc",
			Constants.SITE_PREFIX_PATH + "/my/checktasksucc" }, method = RequestMethod.POST)
	@Transactional(propagation = Propagation.REQUIRED)
	public String checktasksucc(Integer id,String taskid, Integer status,String lockaddr, HttpServletRequest request,
			RedirectAttributes ra, HttpServletResponse response, org.springframework.ui.Model modelMap) throws IOException, DocumentException{
		Response resp = new Response(request, response, modelMap);
		Site site = Context.getCurrentSite();
		Document doc=null;
		Element rootElt=null;
		String	 checksml=CheckXml.CheckTaskSucc(taskid);
		         doc = DocumentHelper.parseText(checksml);
		         rootElt=doc.getRootElement();
		String checkresult=rootElt.elementTextTrim("result");
		String	checkresultremark=CheckXml.ErrorRemark(Integer.valueOf(checkresult));
		System.out.println("checkresult=" + checkresultremark);
		
		return resp.post(1);
	}
	@Value("${allowMmemberChagePassword}")
	private String allowMmemberChagePassword;

	@Autowired
	private CredentialsDigest credentialsDigest;

	@Autowired
	private UserService userService;

	@Autowired
	protected PathResolver pathResolver;

	@Autowired
	protected MessageSource messageSource;
	

	@Autowired
	private SQLService sqlService;
	
	@Autowired
	private OperationLogService logService;
	
	@Autowired
	private IPSeeker ipSeeker;
	
}
