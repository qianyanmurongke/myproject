package com.course.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.course.core.domain.User;
import com.course.core.domain.UserDetail;
import com.course.core.repository.UserDetailDao;
import com.course.core.service.UserDetailService;

@Service
@Transactional(readOnly = true)
public class UserDetailServiceImpl implements UserDetailService {
	public UserDetail get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public UserDetail save(UserDetail detail, User user, String ip) {
		detail.setUser(user);
		detail.setCreationIp(ip);
		detail.applyDefaultValue();
		detail = dao.save(detail);
		user.setDetail(detail);
		return detail;
	}

	@Transactional
	public UserDetail update(UserDetail bean) {
		bean.applyDefaultValue();
		bean = dao.save(bean);
		return bean;
	}

	private UserDetailDao dao;

	@Autowired
	public void setDao(UserDetailDao dao) {
		this.dao = dao;
	}
}
