package com.course.core.repository.plus;

import java.util.List;

import com.course.core.domain.User;

/**
 * UserDaoPlus
 * 
 * @author benfang
 * 
 */
public interface UserDaoPlus {
	public List<User> findByUsername(String[] usernames);
}
