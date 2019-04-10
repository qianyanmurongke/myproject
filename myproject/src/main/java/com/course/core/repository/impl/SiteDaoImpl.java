package com.course.core.repository.impl;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.course.common.orm.JpqlBuilder;
import com.course.common.orm.Limitable;
import com.course.core.domain.Site;
import com.course.core.repository.plus.SiteDaoPlus;

/**
 * SiteDaoImpl
 * 
 * @author benfang
 * 
 */
public class SiteDaoImpl implements SiteDaoPlus {
	@SuppressWarnings("unchecked")
	public List<Site> findByStatus(Integer parentId, String parentNumber, Integer[] status, Limitable limitable) {
		JpqlBuilder jb = new JpqlBuilder("from Site bean");
		if (StringUtils.isNotBlank(parentNumber)) {
			jb.append(" join bean.parent parent");
		}
		jb.append(" where 1=1");
		if (parentId != null) {
			jb.append(" and bean.parent.id = :parentId");
			jb.setParameter("parentId", parentId);
		} else if (StringUtils.isNotBlank(parentNumber)) {
			jb.append(" and parent.number = :parentNumber");
			jb.setParameter("parentNumber", parentNumber);
		}
		if (ArrayUtils.isNotEmpty(status)) {
			jb.append(" and bean.status in (:status)");
			jb.setParameter("status", Arrays.asList(status));
		}
		if (limitable == null) {
			jb.append(" order by bean.treeNumber");
		}
		return jb.list(em, limitable);
	}

	private EntityManager em;

	@PersistenceContext
	public void setEm(EntityManager em) {
		this.em = em;
	}
}
