package com.course.common.web;

/**
 * 路径获取接口
 * 
 * @author benfang
 * 
 */
public interface PathResolver {
	public String getPath(String uri);

	public String getPath(String uri, String prefix);
}
