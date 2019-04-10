package com.course.core.repository.impl;

import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.jpa.QueryHints;

import com.course.core.domain.Attribute;
import com.course.core.domain.dsl.QAttribute;
import com.course.core.repository.plus.AttributeDaoPlus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;

/**
 * AttributeDaoImpl
 * 
 * @author benfang
 * 
 */
public class AttributeDaoImpl implements AttributeDaoPlus {
	public List<Attribute> findByNumbers(String[] numbers, Integer[] siteIds) {
		if (ArrayUtils.isEmpty(numbers)) {
			return Collections.emptyList();
		}
		JPAQuery<Attribute> query = new JPAQuery<Attribute>(this.em);
		query.setHint(QueryHints.HINT_CACHEABLE, true);
		QAttribute attribute = QAttribute.attribute;
		query.from(attribute);
		BooleanBuilder exp = new BooleanBuilder();
		if (ArrayUtils.isNotEmpty(numbers)) {
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = numbers.length; i < len; i++) {
				e = e.or(attribute.number.eq(numbers[i]));
			}
			exp = exp.and(e);
		}
		if (ArrayUtils.isNotEmpty(siteIds)) {
			BooleanBuilder e = new BooleanBuilder();
			for (int i = 0, len = siteIds.length; i < len; i++) {
				e = e.or(attribute.site.id.eq(siteIds[i]));
			}
			exp = exp.and(e);
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
