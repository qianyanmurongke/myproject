package com.course.core.web.back;

import static com.course.core.constant.Constants.CREATE;
import static com.course.core.constant.Constants.DELETE_SUCCESS;
import static com.course.core.constant.Constants.EDIT;
import static com.course.core.constant.Constants.MESSAGE;
import static com.course.core.constant.Constants.OPERATION_SUCCESS;
import static com.course.core.constant.Constants.OPRT;
import static com.course.core.constant.Constants.SAVE_SUCCESS;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.course.common.file.FileHandler;
import com.course.common.image.Images;
import com.course.common.security.CredentialsDigest;
import com.course.common.security.Digests;
import com.course.common.upload.Uploader;
import com.course.common.util.Encodes;
import com.course.common.util.ExportExcelUtil;
import com.course.common.util.ImportExcelUtil;
import com.course.common.web.PathResolver;
import com.course.common.web.Servlets;
import com.course.core.constant.Constants;
import com.course.core.domain.GlobalUpload;
import com.course.core.domain.MemberGroup;
import com.course.core.domain.Model;
import com.course.core.domain.Org;
import com.course.core.domain.PublishPoint;
import com.course.core.domain.Role;
import com.course.core.domain.Site;
import com.course.core.domain.User;
import com.course.core.domain.UserDetail;
import com.course.core.service.MemberGroupService;
import com.course.core.service.ModelService;
import com.course.core.service.OperationLogService;
import com.course.core.service.OrgService;
import com.course.core.service.RoleService;
import com.course.core.service.UserService;
import com.course.core.support.CmsException;
import com.course.core.support.Context;
import com.course.core.support.Response;
/**
 * UserController
 * 
 * @author benfang
 * 
 */
@Controller
@RequestMapping("/core/user")
public class UserController {
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);

	@RequiresPermissions("core:user:list")
	@RequestMapping("list.do")
	public String list(@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		Integer siteId = site.getId();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		User user = Context.getCurrentUser();
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		
		params.put("NOTEQ_type", new String[] { "1" });
		
		Page<User> pagedList = service.findPage(user.getRank(), null, orgTreeNumber, params, pageable);
		List<Org> orgList = orgService.findList(orgTreeNumber);
		List<Role> roleList = roleService.findList(siteId);
		List<MemberGroup> groupList = groupService.findRealGroups();
		modelMap.addAttribute("currentUser", user);
		modelMap.addAttribute("pagedList", pagedList);
		modelMap.addAttribute("orgList", orgList);
		modelMap.addAttribute("roleList", roleList);
		modelMap.addAttribute("groupList", groupList);
		return "core/user/user_list";
	}

	@RequiresPermissions("core:user:create")
	@RequestMapping("create.do")
	public String create(Integer id, Integer orgId, HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		User user = Context.getCurrentUser();
		if (id != null) {
			User bean = service.get(id);
			if (!bean.getOrg().getTreeNumber().startsWith(orgTreeNumber)) {
				throw new CmsException("error.forbiddenData");
			}
			if (user.getRank() > bean.getRank()) {
				throw new CmsException("error.forbiddenData");
			}
			modelMap.addAttribute("bean", bean);
		}
		Org org = null;
		if (orgId != null) {
			org = orgService.get(orgId);
		} else {
			org = site.getOrg();
		}
		modelMap.addAttribute("org", org);
		// 用户属于全局的，获取主站的用户模型。
		Model model = modelService.findDefault(1, User.MODEL_TYPE);
		modelMap.addAttribute("model", model);
		List<Role> roleList = roleService.findList(site.getId());
		List<MemberGroup> groupList = groupService.findRealGroups();
		modelMap.addAttribute("roleList", roleList);
		modelMap.addAttribute("groupList", groupList);
		modelMap.addAttribute("currRank", user.getRank());
		modelMap.addAttribute("orgTreeNumber", orgTreeNumber);
		modelMap.addAttribute(OPRT, CREATE);
		return "core/user/user_form";
	}

	@RequiresPermissions("core:user:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer position,
			@PageableDefault(sort = "id", direction = Direction.DESC) Pageable pageable, HttpServletRequest request,
			org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		User user = Context.getCurrentUser();
		User bean = service.get(id);
		if (!bean.getOrg().getTreeNumber().startsWith(orgTreeNumber)) {
			throw new CmsException("error.forbiddenData");
		}
		if (user.getRank() > bean.getRank()) {
			throw new CmsException("error.forbiddenData");
		}

		// 用户属于全局的，获取主站的用户模型。
		Model model = modelService.findDefault(1, User.MODEL_TYPE);
		modelMap.addAttribute("model", model);
		List<Role> roleList = roleService.findList(site.getId());
		modelMap.addAttribute("roleList", roleList);

		List<MemberGroup> groupList = groupService.findRealGroups();
		modelMap.addAttribute("groupList", groupList);

		List<Org> orgList = orgService.findList(orgTreeNumber);
		modelMap.addAttribute("orgList", orgList);

		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("org", bean.getOrg());
		modelMap.addAttribute("currRank", user.getRank());
		modelMap.addAttribute("orgTreeNumber", orgTreeNumber);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute(OPRT, EDIT);
		return "core/user/user_form";
	}

	@RequiresPermissions("core:user:save")
	@RequestMapping("save.do")
	public String save(User bean, UserDetail detail, Integer[] roleIds, Integer[] orgIds, Integer[] groupIds,
			Integer groupId, String redirect, HttpServletRequest request, RedirectAttributes ra,HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		Site site = Context.getCurrentSite();
		User user = Context.getCurrentUser();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		Org org = orgService.get(1);
		if (!org.getTreeNumber().startsWith(orgTreeNumber)) {
			throw new CmsException("error.forbiddenData");
		}
		Integer currRank = user.getRank();
//		if (!bean.isAdmin()) {
			bean.setRank(User.DEFAULT_RANK);
//		}
//		if (bean.getRank() < currRank) {
//			bean.setRank(currRank);
//		}
//		if (ArrayUtils.isNotEmpty(roleIds)) {
//			for (Integer roleId : roleIds) {
//				Role role = roleService.get(roleId);
//				if (role.getRank() < bean.getRank()) {
//					throw new CmsException("user.error.roleRankHigherThenUserRank");
//				}
//			}
//		}
		Map<String, String> map = Servlets.getParamMap(request, "customs_");
		Map<String, String> clobMap = Servlets.getParamMap(request, "clobs_");
		String ip = Servlets.getRemoteAddr(request);
		service.save(bean, detail, roleIds, orgIds, groupIds, map, clobMap, 1, groupId, ip);
		String userAgent = request.getHeader("user-agent");
		logService.operation("opr.user.add", bean.getUsername(), null, bean.getId(), ip, user.getId(), site.getId(),
				userAgent);
		logger.info("save User, username={}.", bean.getUsername());

		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		return resp.post(1);

	}

	@RequiresPermissions("core:user:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") User bean, @ModelAttribute("detail") UserDetail detail,
			Integer[] roleIds,  Integer[] groupIds, Integer groupId, Integer position,
			String redirect, HttpServletRequest request, RedirectAttributes ra, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);
		Site site = Context.getCurrentSite();
		User currUser = Context.getCurrentUser();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		// orgId = bean.getOrg().getId();
		groupId = bean.getGroup().getId();
		Org org = orgService.get(1);
		if (!org.getTreeNumber().startsWith(orgTreeNumber)) {
			return resp.post(0, "没有权限选择当前所属学校【" + org.getName() + "】");
		}

		// Integer currRank = currUser.getRank();
		// if (!bean.isAdmin()) {
		// bean.setRank(User.DEFAULT_RANK);
		// }

		// if (currRank > bean.getRank()) {
		// bean.setRank(currRank);
		// }

//		 if (ArrayUtils.isNotEmpty(roleIds)) {
//			 for (Integer roleId : roleIds) {
//			 Role role = roleService.get(roleId);
//			 if (role.getRank() < bean.getRank()) {
//			 throw new CmsException("user.error.roleRankHigherThenUserRank");
//			 }
//			 }
//		 }
		Integer topOrgId = site.getOrg().getId();
		Map<String, String> map = Servlets.getParamMap(request, "customs_");
		Map<String, String> clobMap = Servlets.getParamMap(request, "clobs_");

		service.update(bean, detail, roleIds, groupIds, map, clobMap, 1, groupId, topOrgId, site.getId());

		String ip = Servlets.getRemoteAddr(request);
		String userAgent = request.getHeader("user-agent");
		logService.operation("opr.user.edit", bean.getUsername(), null, bean.getId(), ip, currUser.getId(),
				site.getId(), userAgent);
		logger.info("update User, username={}.", bean.getUsername());

		return resp.post(1);
	}

	@RequiresPermissions("core:user:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		User user = Context.getCurrentUser();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		Integer currRank = user.getRank();
		validateIds(ids, orgTreeNumber, currRank);
		for (Integer id : ids) {
			User bean = service.get(id);
			if (bean.getId() == user.getId() || bean.getId() == 0 || bean.getId() == 1) {
				// 当前用户、匿名用户（ID=0）和根用户（ID=1）不能删除
				throw new CmsException("user.error.systemUserCannotBeDeleted");
			}
		}
		User[] beans = service.delete(ids);
		String ip = Servlets.getRemoteAddr(request);
		String userAgent = request.getHeader("user-agent");
		for (User bean : beans) {
			logService.operation("opr.user.delete", bean.getUsername(), null, bean.getId(), ip, user.getId(),
					site.getId(), userAgent);
			logger.info("delete User, username={}.", bean.getUsername());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		return "redirect:list.do";
	}

	// 删除密码
	@RequiresPermissions("core:user:delete_password")
	@RequestMapping("delete_password.do")
	public String deletePassword(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		User user = Context.getCurrentUser();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		Integer currRank = user.getRank();
		validateIds(ids, orgTreeNumber, currRank);
		User[] beans = service.deletePassword(ids);
		String ip = Servlets.getRemoteAddr(request);
		String userAgent = request.getHeader("user-agent");
		for (User bean : beans) {
			logService.operation("opr.user.deletePassword", bean.getUsername(), null, bean.getId(), ip, user.getId(),
					site.getId(), userAgent);
			logger.info("delete User password, username={}..", bean.getUsername());
		}
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	// 审核账户
	@RequiresPermissions("core:user:check")
	@RequestMapping("check.do")
	public String check(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		User user = Context.getCurrentUser();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		Integer currRank = user.getRank();
		validateIds(ids, orgTreeNumber, currRank);
		User[] beans = service.check(ids);
		for (User bean : beans) {
			logService.operation("opr.user.check", bean.getUsername(), null, bean.getId(), request);
			logger.info("check Member, username={}.", bean.getUsername());
		}
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	// 禁用账户
	@RequiresPermissions("core:user:lock")
	@RequestMapping("lock.do")
	public String lock(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		User user = Context.getCurrentUser();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		Integer currRank = user.getRank();
		validateIds(ids, orgTreeNumber, currRank);
		User[] beans = service.lock(ids);
		for (User bean : beans) {
			logService.operation("opr.user.lock", bean.getUsername(), null, bean.getId(), request);
			logger.info("disable User, username={}..", bean.getUsername());
		}
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	// 解禁账户
	@RequiresPermissions("core:user:unlock")
	@RequestMapping("unlock.do")
	public String unlock(Integer[] ids, HttpServletRequest request, RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		User user = Context.getCurrentUser();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		Integer currRank = user.getRank();
		validateIds(ids, orgTreeNumber, currRank);
		User[] beans = service.unlock(ids);
		for (User bean : beans) {
			logService.operation("opr.user.unlock", bean.getUsername(), null, bean.getId(), request);
			logger.info("undisable User, username={}..", bean.getUsername());
		}
		ra.addFlashAttribute(MESSAGE, OPERATION_SUCCESS);
		return "redirect:list.do";
	}

	/**
	 * 检查用户名是否存在
	 */
	@RequestMapping("check_username.do")
	public void checkUsername(String username, String original, HttpServletResponse response) {
		if (StringUtils.isBlank(username)) {
			Servlets.writeHtml(response, "false");
			return;
		}
		if (StringUtils.equals(username, original)) {
			Servlets.writeHtml(response, "true");
			return;
		}
		// 检查数据库是否重名
		boolean exist = service.usernameExist(username);
		if (!exist) {
			Servlets.writeHtml(response, "true");
		} else {
			Servlets.writeHtml(response, "false");
		}
	}

	@ModelAttribute
	public void preloadBean(@RequestParam(required = false) Integer oid, org.springframework.ui.Model modelMap) {
		if (oid != null) {
			User bean = service.get(oid);
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

	private void validateIds(Integer[] ids, String orgTreeNumber, Integer currRank) {
		for (Integer id : ids) {
			User bean = service.get(id);
			if (currRank > bean.getRank()) {
				throw new CmsException("error.forbiddenData");
			}
			if (!bean.getOrg().getTreeNumber().startsWith(orgTreeNumber)) {
				throw new CmsException("error.forbiddenData");
			}
		}
	}

	@RequestMapping("upload_avatar.do")
	public String upload_logo(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request,
			RedirectAttributes ra, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		Map<String, String[]> params = Servlets.getParamValuesMap(request, "avatar_");
		User user = Context.getCurrentUser();
		Response resp = new Response(request, response, modelMap);
		if (file == null || file.isEmpty()) {
			return resp.post(0);
		}
		String origFilename = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origFilename).toLowerCase();

		Site site = Context.getCurrentSite();
		GlobalUpload gu = site.getGlobal().getUpload();
		// 后缀名是否合法
		if (!gu.isExtensionValid(ext, Uploader.IMAGE)) {
			return resp.post(0);
		}
		BufferedImage buffImg = ImageIO.read(file.getInputStream());

		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		// 保存头像原图
		String pathname = "/users/" + user.getId() + "/" + User.AVATAR;

		String urlPrefix = point.getStorePath();
		String fileUrl = urlPrefix + pathname;
		// 一律存储为jpg
		Double left = Double.parseDouble(params.get("x")[0]);
		Double top = Double.parseDouble(params.get("y")[0]);
		Double width = Double.parseDouble(params.get("width")[0]);
		Double height = Double.parseDouble(params.get("height")[0]);
		Double rotate = Double.parseDouble(params.get("rotate")[0]);

		if (left != null && top != null && width != null && height != null) {
			buffImg = Images.crop(buffImg, rotate.intValue(), left.intValue(), top.intValue(), width.intValue(),
					height.intValue());
		}

		fileHandler.storeImage(buffImg, "jpg", pathname);

		// 保存大头像
		String pathnameLarge = "/users/" + user.getId() + "/" + User.AVATAR_LARGE;
		Integer avatarLarge = site.getGlobal().getRegister().getAvatarLarge();
		site.getGlobal().getRegister().getAvatarLarge();
		BufferedImage buffLarge = Scalr.resize(buffImg, Scalr.Method.QUALITY, avatarLarge, avatarLarge);
		fileHandler.storeImage(buffLarge, "jpg", pathnameLarge);
		// 保存小头像
		String pathnameSmall = "/users/" + user.getId() + "/" + User.AVATAR_SMALL;
		Integer avatarSmall = site.getGlobal().getRegister().getAvatarSmall();
		BufferedImage buffSmall = Scalr.resize(buffImg, Scalr.Method.QUALITY, avatarSmall, avatarSmall);
		fileHandler.storeImage(buffSmall, "jpg", pathnameSmall);

		// 删除临时
		// fileHandler.delete(fileUrl);

		resp.addData("fileUrl", fileUrl);
		return resp.post(1);
	}

	@RequiresPermissions("core:user:importexcel")
	@RequestMapping("importexcel.do")
	@Transactional
	public String importexcel(String filepath, HttpServletRequest request, RedirectAttributes ra,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);

		Site site = Context.getCurrentSite();

		if (filepath == null) {
			return resp.post(0, "文件名不能为空.");
		}
        Org org=Context.getCurrentOrg(request);
		User user = null;
		UserDetail detail = null;
		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		String basePath = pathResolver.getPath(point.getStorePath() + "/excel/");

		// 读取Excel数据到List中
		try {
			List<Map<String, String>> list = new ImportExcelUtil().readExcel(basePath + filepath, "Sheet1");

			// 记录行数
			int index = 2;

			for (Map<String, String> map : list) {
				// 姓名
				String realName = map.get("姓名");
				if (realName == "") {
					return resp.post(0, "Excel 第 " + index + " 行： 姓名漏写");
				}

				// 用户名
				String userName = map.get("用户名");
				

				// 手机号码
				String phone = map.get("手机号码");
				if (phone == "") {
					return resp.post(0, "Excel 第 " + index + " 行： 手机号码漏写");
				}
                if(!isPhone(phone)){
                	return resp.post(0, "Excel 第 " + index + " 行： 请输入正确的手机号码");
                }
                if(userName==""){
                	userName=phone;
                }
				//用户组
				String rolename = map.get("用户组");
                Role role=roleService.getByName(rolename);	
                if(role==null){
                	role=new Role();
                	role.setName(rolename);
                	role.setRank(999);
                	roleService.save(role, null, null, site.getId());
                }
                
				String perCode= map.get("一卡通账号");
				// 用户
                user = service.getUserByMobile(phone);
				
				if (user != null) {
					detail = user.getDetail();
				}
				if (user != null) {
					// user表
					user.setRealName(realName);
					user.setMobile(phone);
					user.setCarCode(perCode);
					Integer topOrgId = site.getOrg().getId();
					service.update(user, detail, new Integer[]{role.getId()}, null , null, null, org.getId(), 1, topOrgId, site.getId());
					logger.info("update User, id={}.", user.getId());
				} else {
					// 保存
					boolean exist = service.usernameExist(userName);
					
					if (exist) {
						return resp.post(0, "Excel 第 " + index + " 行： 用户名重复");
					}

					// user表
					user = new User();
					user.setRealName(realName);
					user.setUsername(userName);
					user.setMobile(phone);
					user.setCarCode(perCode);
					// 默认密码
					byte[] saltBytes = Digests.generateSalt(8);
					String salt = Encodes.encodeHex(saltBytes);
					user.setSalt(salt);

					String rawPass = "123456";
					String encPass = credentialsDigest.digest(rawPass, saltBytes);
					user.setPassword(encPass);

					detail = new UserDetail();
					String ip = Servlets.getRemoteAddr(request);
					service.save(user, detail, new Integer[]{role.getId()}, new Integer[] { org.getId() }, null, null, null, org.getId(), 1, ip);
					logger.info("save User, id={}.", user.getId());
				}

				index++;

			}
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		return resp.post(1);
	}
	

	
	public static boolean isPhone(String phone) {
	    String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
	    if (phone.length() != 11) {
	        return false;
	    } else {
	        Pattern p = Pattern.compile(regex);
	        Matcher m = p.matcher(phone);
	        boolean isMatch = m.matches();
	        return isMatch;
	    }
	}

	/**
	 * 
	 * @Description 用户信息导出
	 * @author  wangfei
	 * @date  2019年3月12日上午11:57:47
	 * @param pageable
	 * @param request
	 * @param response
	 */
	@RequestMapping("export.do")
	public void export(@PageableDefault(sort="id",direction=Direction.DESC)Pageable pageable,
			HttpServletRequest request,HttpServletResponse response){
		Map<String, String[]> params=Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		User user=Context.getCurrentUser();
		Site site=Context.getCurrentSite();
		String orgTreeNumber=site.getOrg().getTreeNumber();
		Page<User> pagelist=service.findPage(user.getRank(), null, orgTreeNumber, params, pageable);
		String sheetName="用户数据";
		LinkedHashMap<String,String> fields=new LinkedHashMap<String,String>(){
			private static final long serialVersionUID=1L;
			{
				put("departmentName","部门");
				put("username","用户名");
				put("realName","姓名");
				put("carCode","一卡通账号");
				put("group.name","用户组");
			}
		};
		ExportExcelUtil.ExportExcel(fields, pagelist.getContent(), sheetName, response);
		
	}
	@Autowired
	private OperationLogService logService;
	@Autowired
	private OrgService orgService;
	@Autowired
	private MemberGroupService groupService;
	@Autowired
	private ModelService modelService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService service;
	@Autowired
	protected PathResolver pathResolver;
	@Autowired
	private CredentialsDigest credentialsDigest;

}
