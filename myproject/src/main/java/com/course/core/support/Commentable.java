package com.course.core.support;

import java.util.Collection;

import com.course.core.domain.MemberGroup;
import com.course.core.domain.User;

/**
 * Commentable
 * 
 * @author benfang
 * 
 */
public interface Commentable {
	/**
	 * 增加评论数量
	 * 
	 * @param comments
	 */
	public void addComments(int comments);

	/**
	 * 获取评论状态
	 * 
	 * @param groups
	 * @return
	 */
	public int getCommentStatus(User user, Collection<MemberGroup> groups);
}
