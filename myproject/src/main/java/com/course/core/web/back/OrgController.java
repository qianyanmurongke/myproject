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
import com.course.core.domain.PublishPoint;
import com.course.core.domain.Site;
import com.course.core.service.OperationLogService;
import com.course.core.service.OrgService;
import com.course.core.support.CmsException;
import com.course.core.support.Context;
import com.course.core.support.Response;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.ArrayUtils;
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
import java.util.List;
import java.util.Map;

import static com.course.core.constant.Constants.*;

/**
 * OrgController
 *
 * @author benfang
 */
@Controller
@RequestMapping("/core/org")
public class OrgController {
	private static final Logger logger = LoggerFactory.getLogger(OrgController.class);

	@RequiresPermissions("core:org:list")
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
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		return "core/org/org_list";
	}

	@RequiresPermissions("core:org:create")
	@RequestMapping("create.do")
	public String create(Integer id, Integer parentId, Integer queryParentId, Boolean showDescendants,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		
		Org topOrg = site.getOrg();
		String orgTreeNumber = topOrg.getTreeNumber();
		Org bean = null, parent = topOrg;
		if (id != null) {
			bean = service.get(id);
			if (!bean.getTreeNumber().startsWith(topOrg.getTreeNumber())) {
				throw new CmsException("error.forbiddenData");
			}
		}
		if (bean != null) {
			parent = bean.getParent();
		} else if (parentId != null) {
			parent = service.get(parentId);
		}

		modelMap.addAttribute(OPRT, CREATE);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("parent", parent);
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("showDescendants", showDescendants);
		modelMap.addAttribute("orgTreeNumber", orgTreeNumber);
		modelMap.addAttribute("parent", parent);

		return "core/org/org_add_form";
	}

	@RequiresPermissions("core:org:edit")
	@RequestMapping("edit.do")
	public String edit(Integer id, Integer queryParentId, @RequestParam(defaultValue = "true") boolean showDescendants,
			Integer position, @PageableDefault(sort = "treeNumber", direction = Direction.ASC) Pageable pageable,
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
		return "core/org/org_form";
	}

	@RequiresPermissions("core:org:edit_see")
	@RequestMapping("edit_see.do")
	public String edit_see(Integer id, Integer queryParentId, @RequestParam(defaultValue = "true") Integer position,
			@PageableDefault(sort = "treeNumber", direction = Direction.ASC) Pageable pageable,
			HttpServletRequest request, org.springframework.ui.Model modelMap) {
		Site site = Context.getCurrentSite();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		Org bean = service.get(id);
		if (!bean.getTreeNumber().startsWith(orgTreeNumber)) {
			throw new CmsException("error.forbiddenData");
		}

		modelMap.addAttribute(OPRT, EDIT);
		modelMap.addAttribute("bean", bean);
		modelMap.addAttribute("parent", bean.getParent());
		modelMap.addAttribute("queryParentId", queryParentId);
		modelMap.addAttribute("position", position);
		modelMap.addAttribute("orgTreeNumber", orgTreeNumber);

		return "core/org/org_see";
	}

	/**
	 * 
	 * @author wangfei 修改日期 2018年04月09日 10:50 增加消息推送
	 */
	@RequiresPermissions("core:org:save")
	@RequestMapping("save.do")
	public String save(Org bean, Integer parentId, Integer queryParentId, Boolean showDescendants, String redirect,
			HttpServletRequest request, RedirectAttributes ra, HttpServletResponse response,
			org.springframework.ui.Model modelMap) {
		Response resp = new Response(request, response, modelMap);

		Site site = Context.getCurrentSite();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		Org parent = service.get(parentId);
		if (!parent.getTreeNumber().startsWith(orgTreeNumber)) {
			return resp.post(0, "当前学校无法添加子学校");
		}
		service.save(bean, parentId);

		
		logService.operation("opr.org.add", bean.getName(), null, bean.getId(), request);
		logger.info("save Org, name={}.", bean.getName());

		return resp.post(1);
	}

	/**
	 * 
	 * @author wangfei 修改日期 2018年04月09日 10:51 增加消息推送
	 */
	@RequiresPermissions("core:org:update")
	@RequestMapping("update.do")
	public String update(@ModelAttribute("bean") Org bean, Integer parentId, Integer queryParentId, Integer orgTypeId,
			Integer unitTypeId, Integer schoolTypeId, Integer nationId, Integer cityTypeId, Integer economicsId,
			Integer primaryLanguageId, Integer assistedLanguageId, Boolean showDescendants, Integer position,
			String redirect, HttpServletRequest request, RedirectAttributes ra) {


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
		if (Constants.REDIRECT_LIST.equals(redirect)) {
			return "redirect:list.do";
		} else {
			ra.addAttribute("id", bean.getId());
			ra.addAttribute("position", position);
			return "redirect:edit.do";
		}
	}

	@RequiresPermissions("core:org:batch_update")
	@RequestMapping("batch_update.do")
	public String batchUpdate(Integer[] id, String[] name, String[] number, String[] phone, String[] address,
			Integer queryParentId, Boolean showDescendants, Pageable pageable, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		validateIds(id, orgTreeNumber);
		if (ArrayUtils.isNotEmpty(id)) {
			// 有排序的情况下不更新树结构，以免引误操作。
			boolean isUpdateTree = pageable.getSort() == null;
			Org[] beans = service.batchUpdate(id, name, number, phone, address, isUpdateTree);
			for (Org bean : beans) {
				logService.operation("opr.org.batchEdit", bean.getName(), null, bean.getId(), request);
				logger.info("update Org, name={}.", bean.getName());
			}
		}
		ra.addFlashAttribute(MESSAGE, SAVE_SUCCESS);
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		ra.addFlashAttribute("refreshLeft", true);
		return "redirect:list.do";
	}

	/**
	 * 
	 * @author wangfei 修改日期 2018年04月09日 10:52 增加消息推送
	 */
	@RequiresPermissions("core:org:delete")
	@RequestMapping("delete.do")
	public String delete(Integer[] ids, Integer queryParentId, Boolean showDescendants, HttpServletRequest request,
			RedirectAttributes ra) {
		Site site = Context.getCurrentSite();
		String orgTreeNumber = site.getOrg().getTreeNumber();
		validateIds(ids, orgTreeNumber);
		Org[] beans = service.delete(ids);
		for (Org bean : beans) {
			
			logService.operation("opr.org.delete", bean.getName(), null, bean.getId(), request);
			logger.info("delete Org, name={}.", bean.getName());
		}
		ra.addFlashAttribute(MESSAGE, DELETE_SUCCESS);
		ra.addAttribute("queryParentId", queryParentId);
		ra.addAttribute("showDescendants", showDescendants);
		return "redirect:list.do";
	}

	/**
	 * 上传图片
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
		// 后缀名是否合法
		if (!gu.isExtensionValid(ext, Uploader.IMAGE)) {
			return resp.post(0);
		}
		BufferedImage buffImg = ImageIO.read(file.getInputStream());

		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);
		String pathname = "/org/" + Uploader.randomPathname("");

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

		fileHandler.storeImage(buffImg, "jpg", pathname + "." + ext);

		// 保存大
		String pathnameLarge = pathname + "_200." + ext;
		Integer avatarLarge = 200;
		BufferedImage buffLarge = Scalr.resize(buffImg, Scalr.Method.QUALITY, avatarLarge, avatarLarge);
		fileHandler.storeImage(buffLarge, "jpg", pathnameLarge);
		// 保存小
		String pathnameSmall = pathname + "_100." + ext;
		Integer avatarSmall = 100;
		BufferedImage buffSmall = Scalr.resize(buffImg, Scalr.Method.QUALITY, avatarSmall, avatarSmall);
		fileHandler.storeImage(buffSmall, "jpg", pathnameSmall);
		// 保存小
		String pathnameLesser = pathname + "_50." + ext;
		Integer avatarLesser = 50;
		BufferedImage buffLesser = Scalr.resize(buffImg, Scalr.Method.QUALITY, avatarLesser, avatarLesser);
		fileHandler.storeImage(buffLesser, "jpg", pathnameLesser);
		// 删除临时
		// fileHandler.delete(fileUrl);

		resp.addData("fileUrl", fileUrl + "." + ext);
		return resp.post(1);
	}

	@ModelAttribute("bean")
	public Org preloadBean(@RequestParam(required = false) Integer oid) {
		return oid != null ? service.get(oid) : null;
	}

	private void validateIds(Integer[] ids, String orgTreeNumber) {
		for (Integer id : ids) {
			Org org = service.get(id);
			if (!org.getTreeNumber().startsWith(orgTreeNumber)) {
				throw new CmsException("error.forbiddenData");
			}
		}
	}

	@Autowired
	private OperationLogService logService;

	@Autowired
	private OrgService service;


	@Autowired
	private PathResolver pathResolver;

}
