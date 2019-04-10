package com.course.core.service;

import com.course.core.domain.User;

public interface UserOrgService {
	public void update(User user,Integer orgId, Integer topOrgId);
}
