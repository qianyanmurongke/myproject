package com.course.core.service;

import java.util.List;

import com.course.core.domain.User;
import com.course.core.domain.UserMemberGroup;

public interface UserMemberGroupService {
	public List<UserMemberGroup> update(User user, Integer[] groupIds, Integer groupId);
}
