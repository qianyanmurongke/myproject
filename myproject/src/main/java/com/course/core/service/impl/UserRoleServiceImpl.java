package com.course.core.service.impl;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.course.common.orm.SearchFilter;
import com.course.core.domain.Role;
import com.course.core.domain.User;
import com.course.core.domain.UserRole;
import com.course.core.domain.UserRole.UserRoleId;
import com.course.core.listener.RoleDeleteListener;
import com.course.core.repository.UserRoleDao;
import com.course.core.service.RoleService;
import com.course.core.service.UserRoleService;

@Service
@Transactional(readOnly = true)
public class UserRoleServiceImpl implements UserRoleService, RoleDeleteListener {

	public Page<UserRole> findPage(Map<String, String[]> params, Pageable pageable) {

		return dao.findAll(spec(params), pageable);
	}

	private UserRole findOrCreate(User user, Role role) {
		UserRole bean = dao.findOne(new UserRoleId(user.getId(), role.getId()));
		if (bean == null) {
			bean = new UserRole(user, role);
		}
		return bean;
	}

	@Transactional
	public List<UserRole> update(User user, Integer[] roleIds, Integer siteId) {
		if (roleIds == null) {
			roleIds = new Integer[0];
		}
		List<UserRole> userRoles = user.getUserRoles();
		/*
		 * if (siteId != null) { // 删除本站角色 Set<UserRole> tobeDelete = new
		 * HashSet<UserRole>(); for (UserRole userRole : userRoles) { if
		 * (userRole.getRole().getSite().getId().equals(siteId)) {
		 * tobeDelete.add(userRole); } } userRoles.removeAll(tobeDelete); } else
		 * { // 删除所有角色 userRoles.clear(); }
		 */
		// 再新增
		for (Integer id : roleIds) {
			userRoles.add(findOrCreate(user, roleService.get(id)));
		}
		return userRoles;
	}

	private Specification<UserRole> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<UserRole> sp = SearchFilter.spec(filters, UserRole.class);
		return sp;
	}

	public void preRoleDelete(Integer[] ids) {
		if (ids != null) {
			for (Integer id : ids) {
				Role role = roleService.get(id);
				for (User user : dao.findByUserRolesRoleId(id)) {
					UserRole userRole = new UserRole(user, role);
					user.getUserRoles().remove(userRole);
					dao.delete(userRole);
				}
			}
		}
	}

	/**
	 * 删除
	 * 
	 * @param id
	 * @return
	 */

	@Transactional
	public void delete(User user, Role role) {
		List<UserRole> userRoles = user.getUserRoles();
		Set<UserRole> tobeDelete = new HashSet<UserRole>();
		for (UserRole userRole : userRoles) {
			if (userRole.getRole().getId().equals(role.getId())) {
				tobeDelete.add(userRole);
			}
		}
		userRoles.removeAll(tobeDelete);

	}

	private RoleService roleService;

	@Autowired
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	private UserRoleDao dao;

	@Autowired
	public void setDao(UserRoleDao dao) {
		this.dao = dao;
	}
}
