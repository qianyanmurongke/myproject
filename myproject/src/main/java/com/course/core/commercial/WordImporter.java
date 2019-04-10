package com.course.core.commercial;

import java.io.IOException;
import java.net.URISyntaxException;

import org.htmlparser.util.ParserException;
import org.springframework.web.multipart.MultipartFile;

import com.course.common.web.PathResolver;
import com.course.core.domain.PublishPoint;
import com.course.core.service.AttachmentService;

/**
 * Word文档导入功能类。
 * 
 * @author benfang
 *
 */
public class WordImporter {
	public static String importDoc(MultipartFile file, Integer userId, Integer siteId, String ip, String prefix,
			String path, PublishPoint point, PathResolver pathResolver, AttachmentService attachmentService)
			throws IllegalStateException, IOException, ParserException, URISyntaxException {
		return "";
	}
}
