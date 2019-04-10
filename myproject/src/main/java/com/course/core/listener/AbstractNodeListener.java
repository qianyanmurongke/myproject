package com.course.core.listener;

import com.course.core.domain.Node;

/**
 * AbstractNodeListener
 * 
 * @author benfang
 * 
 */
public abstract class AbstractNodeListener implements NodeListener,
		NodeDeleteListener {
	public void postNodeSave(Node[] beans) {
	}

	public void postNodeUpdate(Node[] beans) {
	}

	public void preNodeDelete(Integer[] ids) {
	}

	public void postNodeDelete(Node[] beans) {
	}
}
