package com.course.core.web.back;

import com.course.common.file.FileHandler;
import com.course.common.image.Images;
import com.course.common.orm.RowSide;
import com.course.common.upload.Uploader;
import com.course.common.web.PathResolver;
import com.course.common.web.Servlets;
import com.course.core.constant.Constants;
import com.course.core.domain.GlobalUpload;
import com.course.core.domain.Org;
import com.course.core.domain.OrgTemplate;
import com.course.core.domain.PublishPoint;
import com.course.core.domain.Site;
import com.course.core.service.OperationLogService;
import com.course.core.service.OrgService;
import com.course.core.service.OrgTemplateService;
import com.course.core.support.CmsException;
import com.course.core.support.Context;
import com.course.core.support.Response;
import com.foxinmy.weixin4j.cache.FileCacheStorager;
import com.foxinmy.weixin4j.exception.WeixinException;
import com.foxinmy.weixin4j.model.Token;
import com.foxinmy.weixin4j.model.WeixinAccount;
import com.foxinmy.weixin4j.mp.WeixinProxy;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.course.core.constant.Constants.*;

/**
 * OrgController
 *
 * @author benfang
 */
@Controller
@RequestMapping("/core/myorg")
public class MyOrgController {
	private static final Logger logger = LoggerFactory.getLogger(MyOrgController.class);

	@RequiresPermissions("core:myorg:list")
	@RequestMapping("list.do")
	public String list(Integer queryParentId, @RequestParam(defaultValue = "true") boolean showDescendants,
			@PageableDefault(sort = "treeNumber", direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		String topTreeNumber = site.getOrg().getTreeNumber();
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		List<Org> list = service.findList(topTreeNumber, queryParentId, showDescendants, params, pageable.getSort());
		List<Org> orgList = service.findList(topTreeNumber);
		modelMap.addAttribute("list", list);
		modelMap.addAttribute("orgList", orgList);
		
		Org org = Context.getCurrentOrg(request);
		modelMap.addAttribute("org", org);
			
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		return "core/myorg/org_list";
	}

	@RequiresPermissions("core:myorg:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer queryParentId, @RequestParam(defaultValue = "true") boolean showDescendants,
			Integer position, @PageableDefault(sort = "treeNumber", direction = Direction.ASC) Pageable pageable,HttpServletResponse response,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		
		
		Site site = Context.getCurrentSite();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		Org bean = service.get(id);
		if (!bean.getTreeNumber().startsWith(orgTreeNumber)) {
			throw new CmsException("error.forbiddenData");
		}
		Map<String, String[]> params = Servlets.getParamValuesMap(request, Constants.SEARCH_PREFIX);
		RowSide<Org> side = service.findSide(orgTreeNumber, queryParentId, showDescendants, params, bean, position,
				pageable.getSort());


		modelMap.addAttribute(OPRT, EDIT);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("parent", bean.getParent());
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		modelMap.addAttribute("side", side);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute("orgTreeNumber", orgTreeNumber);

		return "core/myorg/org_form";
	}

	/**
	 * 
	 * @author wangfei 淇敼鏃ユ湡 2018骞�04鏈�09鏃� 10:51 澧炲姞娑堟伅鎺ㄩ��
	 */
	@RequiresPermissions("core:myorg:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Org bean, Integer parentId, Integer queryParentId, Integer orgTypeId,
			Integer unitTypeId, Integer schoolTypeId, Integer nationId, Integer cityTypeId, Integer economicsId,
			Integer primaryLanguageId, Integer assistedLanguageId, Boolean showDescendants, Integer position,
			String redirect, HttpServletRequest request, RedirectAttributes ra,HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		
		Response resp = new Response(request, response, modelMap);		


		Site site = Context.getCurrentSite();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		if (parentId != null) {
			Org parent = service.get(parentId);
			if (!parent.getTreeNumber().startsWith(orgTreeNumber) || !bean.getTreeNumber().startsWith(orgTreeNumber)) {
				throw new CmsException("error.forbiddenData");
			}
		}
		service.update(bean, parentId);
		
		logService.operation("opr.org.edit", bean.getName(), null, bean.getId(), request);
		logger.info("update Org, name={}.", bean.getName());
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		
		return resp.post(1);
		
	}

	/**
	 * 涓婁紶鍥剧墖
	 * 
	 * @param file
	 * @param request
	 * @param ra
	 * @param response
	 * @param modelMap
	 * @return
	 * @throws IOException
	 */
	@RequestMapping("upload_logo.do")
	public String upload_logo(@RequestParam(value = "file") MultipartFile file, HttpServletRequest request,
			RedirectAttributes ra, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		Map<String, String[]> params = Servlets.getParamValuesMap(request, "avatar_");
		Response resp = new Response(request, response, modelMap);
		if (file == null || file.isEmpty()) {
			return resp.post(0);
		}
		String origFilename = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origFilename).toLowerCase();

		Site site = Context.getCurrentSite();
		GlobalUpload gu = site.getGlobal().getUpload();
		// 鍚庣紑鍚嶆槸鍚﹀悎娉�
		if (!gu.isExtensionValid(ext, Uploader.IMAGE)) {
			return resp.post(0);
		}
		BufferedImage buffImg = ImageIO.read(file.getInputStream());

		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		String pathname = "/org/" + Uploader.randomPathname("");

		String urlPrefix = point.getStorePath();
		String fileUrl = urlPrefix + pathname;
		// 涓�寰嬪瓨鍌ㄤ负jpg
		Double left = Double.parseDouble(params.get("x")[0]);
		Double top = Double.parseDouble(params.get("y")[0]);
		Double width = Double.parseDouble(params.get("width")[0]);
		Double height = Double.parseDouble(params.get("height")[0]);
		Double rotate = Double.parseDouble(params.get("rotate")[0]);

		if (left != null && top != null && width != null && height != null) {
			buffImg = Images.crop(buffImg, rotate.intValue(), left.intValue(), top.intValue(), width.intValue(),
					height.intValue());
		}

		fileHandler.storeImage(buffImg, "jpg", pathname + "." + ext);

		// 淇濆瓨澶�
		String pathnameLarge = pathname + "_200." + ext;
		Integer avatarLarge = 200;
		BufferedImage buffLarge = Scalr.resize(buffImg, Scalr.Method.QUALITY, avatarLarge, avatarLarge);
		fileHandler.storeImage(buffLarge, "jpg", pathnameLarge);
		// 淇濆瓨灏�
		String pathnameSmall = pathname + "_100." + ext;
		Integer avatarSmall = 100;
		BufferedImage buffSmall = Scalr.resize(buffImg, Scalr.Method.QUALITY, avatarSmall, avatarSmall);
		fileHandler.storeImage(buffSmall, "jpg", pathnameSmall);
		// 淇濆瓨灏�
		String pathnameLesser = pathname + "_50." + ext;
		Integer avatarLesser = 50;
		BufferedImage buffLesser = Scalr.resize(buffImg, Scalr.Method.QUALITY, avatarLesser, avatarLesser);
		fileHandler.storeImage(buffLesser, "jpg", pathnameLesser);
		// 鍒犻櫎涓存椂
		// fileHandler.delete(fileUrl);

		resp.addData("fileUrl", fileUrl + "." + ext);
		return resp.post(1);
	}

	
	/**
	 * 妯℃澘娑堟伅鍚屾
	 * @return
	 */
	@RequestMapping("templatesynchronization.do")
	public String templateSynchronization(HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {
		
		Response resp = new Response(request, response, modelMap);
		
		Org org = Context.getCurrentOrg(request);
		
		List<OrgTemplate> orgTemplateList = orgTemplateService.findOrgTemplateListByOrgId(org.getId());
		
		WeixinProxy weixinProxy = new WeixinProxy(new WeixinAccount(org.getPublicNumber(), org.getAppSecret()),
				new FileCacheStorager<Token>());				
		
		if(orgTemplateList!= null) {
			
			for(int i =0;i<orgTemplateList.size();i++) {
				
				try {
					weixinProxy.deleteTemplate(orgTemplateList.get(i).getTemplate());
				} catch (WeixinException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}				
				
				orgTemplateService.delete(orgTemplateList.get(i).getId());			
				
			}
		}

		List<String> code = new ArrayList<String>() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 878526561722037478L;

			{
				add("OPENTM416647169");
				add("OPENTM401626018");				
				add("OPENTM411546026");
				add("TM00376");
				add("OPENTM414564177");
				add("OPENTM414429865");
				add("OPENTM401300431");
				add("OPENTM205807073");
				add("OPENTM401300431");
				add("OPENTM204533457");
				add("OPENTM204533457");
				add("OPENTM204533457");
			}
		};
		
		//根据传入的学校公众号id,秘钥
		if (StringUtils.isBlank(org.getPublicNumber()) || StringUtils.isBlank(org.getAppSecret())) {

			return resp.post(0,"获取学校信息成功");
		}
		
		
		List<String> template = new ArrayList<String>();		
		
		for(int i = 0;i<code.size();i++) {
			
			try {
				
				template.add(weixinProxy.getTemplateId(code.get(i)));
			} catch (WeixinException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}		
				
		List<String> templateType = new ArrayList<String>() {
		
			/**
			 * 
			 */
			private static final long serialVersionUID = -4340745635524460828L;

			{
				add("repairapply");
				add("changecourse");
				add("studentleave");
				add("homework");				
				add("teacherleave");			
				add("meeting");
				add("replacecourse");
				add("electivetask");
				add("replaceclass");
				add("classnotice");
				add("notice");
				add("classmain");
			}
		};
		
		for(int i =0;i<code.size();i++) {			
			OrgTemplate orgTemplate = new OrgTemplate();			
			orgTemplate.setOrg(org);
			orgTemplate.setCode(code.get(i));
			orgTemplate.setTemplate(template.get(i));
			orgTemplate.setTemplateType(templateType.get(i));			
			orgTemplateService.save(orgTemplate);
		
		}
	
		return resp.post(1);
	}		
	
	@ModelAttribute("bean")
	public Org preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}



	@Autowired
	private OperationLogService logService;

	@Autowired
	private OrgService service;

	@Autowired
	private PathResolver pathResolver;

	
	@Autowired
	private OrgTemplateService orgTemplateService;
}
