package com.course.core.service;

import com.course.core.domain.Node;
import com.course.core.domain.Role;

public interface NodeRoleService {
	public void update(Role role, Integer[] infoPermIds, Integer[] nodePermIds);

	public void update(Node node, Integer[] infoPermIds, Integer[] nodePermIds);
}
