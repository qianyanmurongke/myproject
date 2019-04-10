package com.course.core.commercial;

import java.io.IOException;
import java.net.URISyntaxException;

import org.htmlparser.util.ParserException;

import com.course.common.file.FileHandler;
import com.course.core.service.AttachmentService;

/**
 * 处理由word文档（doc、docx）转换成的HTML。
 * 
 * @author benfang
 *
 */
public class WordHTMLResolver {
	public static String resolver(String filename, String prefix, String path, FileHandler fileHandler, String ip,
			Integer userId, Integer siteId, AttachmentService attachmentService) throws ParserException,
			URISyntaxException, IllegalStateException, IOException {
		return "";
	}
}
