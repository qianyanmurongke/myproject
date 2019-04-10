package com.course.core.service;

import com.course.core.domain.User;
import com.course.core.domain.UserDetail;

public interface UserDetailService {
	public UserDetail get(Integer id);

	public UserDetail save(UserDetail detail, User user, String ip);

	public UserDetail update(UserDetail detail);
}
