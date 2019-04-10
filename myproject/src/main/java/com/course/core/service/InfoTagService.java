package com.course.core.service;

import com.course.core.domain.Info;

public interface InfoTagService {
	public void update(Info info, String[] tagNames);

	public void deleteByTagId(Integer tagId);
}
