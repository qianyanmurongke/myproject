package com.course.core.html;

import java.io.IOException;

import com.course.core.domain.Info;
import com.course.core.domain.Node;

import freemarker.template.TemplateException;

/**
 * HtmlService
 * 
 * @author benfang
 * 
 */
public interface HtmlService {
	public void makeInfo(Integer infoId);

	public void makeInfo(Info info);

	public void deleteInfo(Info info);

	public void makeNode(Integer nodeId);

	public void makeNode(Node node);

	public void makeHome(Integer siteId) throws IOException, TemplateException;

	public Integer makeNode(Integer siteId, Integer nodeId,
			boolean includeChildren, Integer taskId, boolean forUpdate,
			Integer lastId, int maxResult) throws IOException,
			TemplateException;

	public Integer makeInfo(Integer siteId, Integer nodeId,
			boolean includeChildren, Integer taskId, boolean forUpdate,
			Integer lastId, int maxResult) throws IOException,
			TemplateException;
}
