package com.course.core.web.back.f7;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.course.common.file.FileHandler;
import com.course.common.web.PathResolver;
import com.course.common.web.Servlets;
import com.course.core.domain.PublishPoint;
import com.course.core.domain.Site;
import com.course.core.support.Context;

@Controller
@RequestMapping("/commons")
public class ImportExcelF7Controller {

	@RequestMapping("importexcel.do")
	public String importExcel(Integer id, Integer excludeChildrenId, Boolean isRealNode, HttpServletRequest request,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {

		Servlets.setNoCacheHeader(response);
		return "commons/import_excel";
	}

	@RequestMapping("uploadclasslogofile.do")
	public void uploadClassLogoSubmit(@RequestParam(value = "file") MultipartFile file, String md5, String name,
			String size, Integer chunks, Integer chunk, HttpServletRequest request, RedirectAttributes ra,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {

		if (file == null) {

			Servlets.writeHtml(response, "{\"error\":1,\"message\":\"文件不能为空.\"}");
			return;
		}

		Site site = Context.getCurrentSite();
		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);

		String basePath = point.getStorePath() + "/common/" + site.getId().toString() + "/";

		String fullPath = pathResolver.getPath(point.getStorePath() + "/common/" + site.getId().toString() + "/");

		this.SaveFile(fileHandler, fullPath, basePath, file, md5, name, size, chunks, chunk, response);
	}

	@RequestMapping("uploadhomeworkfile.do")
	public void uploadHomeworkFile(@RequestParam(value = "file") MultipartFile file, String md5, String name,
			String size, Integer chunks, Integer chunk, HttpServletRequest request, RedirectAttributes ra,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {

		if (file == null) {

			Servlets.writeHtml(response, "{\"error\":1,\"message\":\"文件不能为空.\"}");
			return;
		}

		Site site = Context.getCurrentSite();
		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);

		String basePath = point.getStorePath() + "/homework/" + site.getId().toString() + "/";

		String fullPath = pathResolver.getPath(point.getStorePath() + "/homework/" + site.getId().toString() + "/");

		this.SaveFile(fileHandler, fullPath, basePath, file, md5, name, size, chunks, chunk, response);
	}

	@RequestMapping("uploadvideofile.do")
	public void uploadVieoFile(@RequestParam(value = "file") MultipartFile file, String md5, String name,
			String size, Integer chunks, Integer chunk, HttpServletRequest request, RedirectAttributes ra,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {

		if (file == null) {

			Servlets.writeHtml(response, "{\"error\":1,\"message\":\"文件不能为空.\"}");
			return;
		}

		Site site = Context.getCurrentSite();
		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);

		String basePath = point.getUrlPrefix() + "/video/" + site.getId().toString() + "/";

		String fullPath = pathResolver.getPath(point.getStorePath() + "/video/" + site.getId().toString() + "/");

		this.SaveFile(fileHandler, fullPath, basePath, file, md5, name, size, chunks, chunk, response);
	}

	@RequestMapping("uploadclassmienfile.do")
	public void uploadfileSubmit(@RequestParam(value = "file") MultipartFile file, String md5, String name, String size,
			Integer chunks, Integer chunk, HttpServletRequest request, RedirectAttributes ra,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {

		if (file == null) {

			Servlets.writeHtml(response, "{\"error\":1,\"message\":\"文件不能为空.\"}");
			return;
		}

		Site site = Context.getCurrentSite();
		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);

		String basePath = point.getStorePath() + "/classmien/" + site.getId().toString() + "/";

		String fullPath = pathResolver.getPath(point.getStorePath() + "/classmien/" + site.getId().toString() + "/");

		this.SaveFile(fileHandler, fullPath, basePath, file, md5, name, size, chunks, chunk, response);
	}
	
	
	@RequestMapping("uploadsocialpracticefile.do")
	public void uploadsocialpracticefileSubmit(@RequestParam(value = "file") MultipartFile file, String md5, String name, String size,
			Integer chunks, Integer chunk, HttpServletRequest request, RedirectAttributes ra,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {

		if (file == null) {

			Servlets.writeHtml(response, "{\"error\":1,\"message\":\"文件不能为空.\"}");
			return;
		}

		Site site = Context.getCurrentSite();
		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);

		String basePath = point.getStorePath() + "/socialpractice/" + site.getId().toString() + "/";

		String fullPath = pathResolver.getPath(point.getStorePath() + "/socialpractice/" + site.getId().toString() + "/");

		this.SaveFile(fileHandler, fullPath, basePath, file, md5, name, size, chunks, chunk, response);
	}
	
	
	@RequestMapping("importexportsubmit.do")
	public void importexportSubmit(@RequestParam(value = "file") MultipartFile file, String md5, String name,
			String size, Integer chunks, Integer chunk, HttpServletRequest request, RedirectAttributes ra,
			HttpServletResponse response, org.springframework.ui.Model modelMap) {

		if (file == null) {

			Servlets.writeHtml(response, "{\"error\":1,\"message\":\"文件不能为空.\"}");
			return;
		}
		// String name = file.getOriginalFilename();
		// long size = file.getSize();
		// if (name == null || ExcelUtil.EMPTY.equals(name) && size == 0) {
		// return resp.post(0, "文件不能为空");
		// }
		// // 读取Excel数据到List中
		// List<ArrayList<String>> list = new ImportExcelUtil().readExcel(file);

		Site site = Context.getCurrentSite();
		PublishPoint point = site.getGlobal().getUploadsPublishPoint();
		FileHandler fileHandler = point.getFileHandler(pathResolver);

		String basePath = point.getStorePath() + "/excel/";

		String fullPath = pathResolver.getPath(point.getStorePath() + "/excel/");

		this.SaveFile(fileHandler, fullPath, basePath, file, md5, name, size, chunks, chunk, response);
	}

	public void SaveFile(FileHandler fileHandler, String fullPath, String basePath, MultipartFile multipartFile,
			String md5, String name, String size, Integer chunks, Integer chunk, HttpServletResponse response) {
		String fileType = name.substring(name.lastIndexOf('.'));

		String dirblock = fullPath + md5 + "/";

		File paramFile = new File(fullPath + md5 + fileType);

		File dirblockFile = new File(fullPath + md5);

		if (paramFile.exists() && dirblockFile.exists()) // 判断文件是否存在并判断文件块是否还有
		{
			// 组装文件块
			WriteFile(paramFile, chunks, dirblock, md5);

			Servlets.writeHtml(response,
					"{\"error\":\"0\",\"id\":\"" + md5 + fileType + "\",\"basePath\":\"" + basePath + "\"}");
			return;
		} else if (paramFile.exists()) {
			Servlets.writeHtml(response,
					"{\"error\":\"0\",\"id\":\"" + md5 + fileType + "\",\"basePath\":\"" + basePath + "\"}");
			return;
		}

		// 如果目录不存在，则创建目录
		if (!dirblockFile.exists()) {
			dirblockFile.mkdirs();
		}
		try {
			File chunkFile = new File(dirblock + chunk + "." + md5);
			if (!chunkFile.exists()) {

				chunkFile.createNewFile();

			} else {
				chunkFile.delete();
				chunkFile.createNewFile();
			}

			multipartFile.transferTo(chunkFile);

		} catch (IllegalStateException | IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}

		File[] fileList = dirblockFile.listFiles();

		if (fileList.length >= chunks) {
			WriteFile(paramFile, chunks, dirblock, md5);
			Servlets.writeHtml(response,
					"{\"error\":\"0\",\"id\":\"" + md5 + fileType + "\",\"basePath\":\"" + basePath + "\"}");
			return;
		}

	}

	public void WriteFile(File file, int total, String dirblock, String md5) {

		try {
			int bufSize = 1024;
			byte[] buffer = new byte[bufSize];
			BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(file));

			for (int i = 0; i < total; i++) {
				String part = dirblock + Integer.toString(i) + "." + md5;
				File partFile = new File(part);
				if (partFile.exists()) {

					BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(partFile));
					int readcount;
					while ((readcount = inputStream.read(buffer)) > 0) {
						outputStream.write(buffer, 0, readcount);
					}
					inputStream.close();
					partFile.delete();
				}
			}

			outputStream.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		File dirblockFile = new File(dirblock);
		if (dirblockFile.exists()) {
			dirblockFile.delete();
		}
	}

	@Autowired
	private PathResolver pathResolver;
}
