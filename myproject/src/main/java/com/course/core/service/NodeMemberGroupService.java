package com.course.core.service;

import com.course.core.domain.MemberGroup;
import com.course.core.domain.Node;

public interface NodeMemberGroupService {
	public void update(MemberGroup group, Integer[] viewNodeIds,
			Integer[] contriNodeIds, Integer[] commentNodeIds, Integer siteId);

	public void update(Node node, Integer[] viewGroupIds,
			Integer[] contriGroupIds, Integer[] commentGroupIds);
}
