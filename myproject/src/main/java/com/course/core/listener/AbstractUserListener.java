package com.course.core.listener;

import com.course.core.domain.Info;

/**
 * AbstractUserListener
 * 
 * @author benfang
 * 
 */
public abstract class AbstractUserListener implements UserDeleteListener {
	public void postSave(Info[] beans) {
	}

	public void postUpdate(Info[] beans) {
	}

	public void preUserDelete(Integer[] ids) {
	}

	public void postDelete(Info[] beans) {
	}
}
