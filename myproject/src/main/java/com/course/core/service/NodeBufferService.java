package com.course.core.service;

import com.course.core.domain.Node;
import com.course.core.domain.NodeBuffer;

/**
 * NodeBufferService
 * 
 * @author benfang
 * 
 */
public interface NodeBufferService {
	public NodeBuffer get(Integer id);

	public NodeBuffer save(NodeBuffer bean, Node node);

	public int updateViews(Integer id);
}
