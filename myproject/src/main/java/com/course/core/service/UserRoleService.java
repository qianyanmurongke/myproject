package com.course.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.course.core.domain.Role;
import com.course.core.domain.User;
import com.course.core.domain.UserRole;

public interface UserRoleService {

	public Page<UserRole> findPage(Map<String, String[]> params, Pageable pageable);

	public List<UserRole> update(User user, Integer[] roleIds, Integer siteId);
	

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */
	public void delete(User user, Role role);
}
