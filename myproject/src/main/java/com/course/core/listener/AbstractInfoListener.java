package com.course.core.listener;

import java.util.List;

import com.course.core.domain.Info;

/**
 * AbstractInfoListener
 * 
 * @author benfang
 * 
 */
public abstract class AbstractInfoListener implements InfoListener {
	public void postInfoSave(Info bean) {
	}

	public void postInfoUpdate(Info bean) {
	}

	public void postInfoDelete(List<Info> beans) {
	}

	public void postInfoPass(List<Info> beans) {
	}

	public void postInfoReject(List<Info> beans) {
	}

	public void postInfoMove(List<Info> beans) {
	}

	public void postInfoArchive(List<Info> beans) {
	}

	public void postInfoLogicDelete(List<Info> beans) {
	}

	public void postInfoRecall(List<Info> beans) {
	}

	public void postInfoRecycle(List<Info> beans) {
	}
}
