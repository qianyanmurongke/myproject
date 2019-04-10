package com.course.core.commercial;

import java.io.File;

import com.course.common.file.FileHandler;
import com.course.core.service.AttachmentService;

/**
 * 上传DOC文件。将doc文件转换成pdf，将pdf文件转化成swf文件。
 * 
 * @author benfang
 *
 */
public class UploadDoc {
	public static void exec(AttachmentService attachmentService, FileHandler fileHandler, String pathname,
			String extension, String pdfPathname, String swfPathname, File file, String ip, Integer userId,
			Integer siteId) throws Exception {
		
	}
}
