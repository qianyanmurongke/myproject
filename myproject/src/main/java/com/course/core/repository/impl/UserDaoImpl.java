package com.course.core.repository.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.jpa.QueryHints;

import com.course.core.domain.User;
import com.course.core.domain.dsl.QUser;
import com.course.core.repository.plus.UserDaoPlus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * UserDaoImpl
 * 
 * @author benfang
 * 
 */
public class UserDaoImpl implements UserDaoPlus {
	public List<User> findByUsername(String[] usernames) {
		if (ArrayUtils.isEmpty(usernames)) {
			return Collections.emptyList();
		}
		JPAQuery<User> query = new JPAQuery<User>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QUser user = QUser.user;
		query.from(user);
		BooleanBuilder exp = new BooleanBuilder();
		for (int i = 0, len = usernames.length; i < len; i++) {
			exp = exp.or(user.username.eq(usernames[i]));
		}
		query.where(exp);
		return query.fetch();
	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
