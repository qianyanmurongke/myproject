package com.course.core.web.back;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.course.common.file.FileHandler;
import com.course.core.domain.PublishPoint;
import com.course.core.domain.Site;

/**
 * WebFileController
 * 
 * @author benfang
 * 
 */
@Controller
@RequestMapping("/core/web_file_2")
public class WebFileUploadsController extends WebFileControllerAbstractor {
	@Override
	protected int getType() {
		return UPLOAD;
	}

	@Override
	protected String getBase(Site site) {
		return site.getSiteBase("");
	}

	@Override
	protected String getDefPath(Site site) {
		return getBase(site);
	}

	@Override
	protected String getUrlPrefix(Site site) {
		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		return point.getUrlPrefix();
	}

	@Override
	protected FileHandler getFileHandler(Site site) {
		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		return point.getFileHandler(pathResolver);
	}

	@Override
	protected boolean isFtp(Site site) {
		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		return point.isFtpMethod();
	}

	@RequiresPermissions("core:web_file_2:left")
	@RequestMapping("left.do")
	public String left(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		return super.left(request, response, modelMap);
	}

	@RequiresPermissions("core:web_file_2:left")
	@RequestMapping("left_tree.do")
	public String leftTree(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException {
		return super.leftTree(request, response, modelMap);
	}

	@RequiresPermissions("core:web_file_2:list")
	@RequestMapping("list.do")
	public String list(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		return super.list(request, response, modelMap);
	}

	@RequiresPermissions("core:web_file_2:create")
	@RequestMapping("create.do")
	public String create(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		return super.create(request, response, modelMap);
	}

	@RequiresPermissions("core:web_file_2:edit")
	@RequestMapping("edit.do")
	public String edit(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		return super.edit(request, response, modelMap);
	}

	@RequiresPermissions("core:web_file_2:mkdir")
	@RequestMapping(value = "mkdir.do", method = RequestMethod.POST)
	public String mkdir(String parentId, String dir, HttpServletRequest request, HttpServletResponse response,
			RedirectAttributes ra) throws IOException {
		return super.mkdir(parentId, dir, request, response, ra);
	}

	@RequiresPermissions("core:web_file_2:save")
	@RequestMapping(value = "save.do", method = RequestMethod.POST)
	public String save(String parentId, String name, String text, String redirect, HttpServletRequest request,
			HttpServletResponse response, RedirectAttributes ra) throws IOException {
		return super.save(parentId, name, text, redirect, request, response, ra);
	}

	@RequiresPermissions("core:web_file_2:update")
	@RequestMapping(value = "update.do", method = RequestMethod.POST)
	public void update(String parentId, String origName, String name, String text, Integer position, String redirect,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		super.update(parentId, origName, name, text, position, redirect, request, response);
	}

	@RequiresPermissions("core:web_file_2:delete")
	@RequestMapping("delete.do")
	public String delete(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		return super.delete(request, response, ra);
	}

	@RequiresPermissions("core:web_file_2:rename")
	@RequestMapping("rename.do")
	public String rename(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		return super.rename(request, response, ra);
	}

	@RequiresPermissions("core:web_file_2:move")
	@RequestMapping("move.do")
	public String move(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		return super.move(request, response, ra);
	}

	@RequiresPermissions("core:web_file_2:zip")
	@RequestMapping("zip.do")
	public String zip(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		return super.zip(request, response, ra);
	}

	@RequiresPermissions("core:web_file_2:zip_download")
	@RequestMapping("zip_download.do")
	public void zipDownload(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		super.zipDownload(request, response, ra);
	}

	@RequiresPermissions("core:web_file_2:unzip")
	@RequestMapping("unzip.do")
	public String unzip(HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra)
			throws IOException {
		return super.unzip(request, response, ra);
	}

	@RequiresPermissions("core:web_file_2:upload")
	@RequestMapping("upload.do")
	public void upload(@RequestParam(value = "file", required = false) MultipartFile file, String parentId,
			HttpServletRequest request, HttpServletResponse response) throws IllegalStateException, IOException {
		super.upload(file, parentId, request, response);
	}

	@RequiresPermissions("core:web_file_2:zip_upload")
	@RequestMapping("zip_upload.do")
	public void zipUpload(@RequestParam(value = "file", required = false) MultipartFile file, String parentId,
			HttpServletRequest request, HttpServletResponse response, RedirectAttributes ra) throws IOException {
		super.zipUpload(file, parentId, request, response, ra);
	}

	@RequestMapping("choose_dir.do")
	protected String dir(HttpServletRequest request, HttpServletResponse response, org.springframework.ui.Model modelMap)
			throws IOException {
		return super.dir(request, response, modelMap);
	}

	@RequestMapping("choose_dir_list.do")
	public String dirList(HttpServletRequest request, HttpServletResponse response,
			org.springframework.ui.Model modelMap) throws IOException {
		return super.dirList(request, response, modelMap);
	}
}
