package com.course.core.listener;

import com.course.core.domain.Node;

/**
 * NodeListener
 * 
 * @author benfang
 * 
 */
public interface NodeListener {
	public void postNodeSave(Node[] beans);

	public void postNodeUpdate(Node[] beans);

	public void postNodeDelete(Node[] beans);
}
