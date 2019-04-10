package com.course.core.html;

import com.course.core.domain.Node;

public interface HtmlGenerator {

	public void makeAll(Integer siteId, String siteName, Integer userId,
			boolean forUpdate);

	public void makeInfo(Integer siteId, Integer nodeId, String nodeName,
			boolean includeChildren, Integer userId);

	public void makeNode(Integer siteId, Integer nodeId, String nodeName,
			boolean includeChildren, Integer userId);

	public void deleteHtml(Node node);
}
