package com.course.core.support;

import static com.course.common.upload.Uploader.QUICK_UPLOAD;
import static com.course.common.upload.Uploader.THUMBNAIL;

import org.apache.commons.lang3.StringUtils;

import com.course.common.upload.Uploader;

/**
 * UploadUtils
 * 
 * @author benfang
 * 
 */
public class UploadUtils {

	public static String getUrl(Integer siteId, String type, String extension) {
		StringBuilder name = new StringBuilder();
		name.append('/').append(siteId);
		name.append('/').append(type);
		name.append('/').append(QUICK_UPLOAD);
		name.append(Uploader.randomPathname(extension));
		return name.toString();
	}

	public static String getThumbnailPath(String path) {
		if (StringUtils.isBlank(path)) {
			return path;
		}
		int index = path.lastIndexOf('.');
		if (index != -1) {
			return path.substring(0, index) + THUMBNAIL + path.substring(index);
		} else {
			return path;
		}
	}
}
