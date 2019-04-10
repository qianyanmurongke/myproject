package com.course.core.service;

import com.course.core.domain.Node;
import com.course.core.domain.NodeDetail;

/**
 * NodeDetailService
 * 
 * @author benfang
 * 
 */
public interface NodeDetailService {
	public void save(NodeDetail detail, Node node);

	public NodeDetail update(NodeDetail detail, Node node);
}
