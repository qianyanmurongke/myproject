package com.course.core.html;

import java.io.IOException;

import com.course.common.web.PathResolver;
import com.course.core.service.TaskService;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

/**
 * HtmlDao
 * 
 * @author benfang
 * 
 */
public interface HtmlDao {
	public int makeNode(Integer siteId, Integer nodeId, String treeNumber,
			Configuration config, PathResolver pathResolver,
			TaskService taskService, Integer taskId, boolean forUpdate)
			throws IOException, TemplateException;

	public int makeInfo(Integer siteId, Integer nodeId, String treeNumber,
			Configuration config, PathResolver pathResolver,
			TaskService taskService, Integer taskId, boolean forUpdate)
			throws IOException, TemplateException;
}
