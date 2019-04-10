package com.course.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import com.course.common.orm.Limitable;
import com.course.core.domain.Model;
import com.course.core.repository.plus.ModelDaoPlus;

/**
 * ModelDao
 * 
 * @author benfang
 * 
 */
public interface ModelDao extends Repository<Model, Integer>, ModelDaoPlus {
	public List<Model> findAll(Specification<Model> spec, Sort sort);

	public List<Model> findAll(Specification<Model> spec, Limitable limit);

	public Model findOne(Integer id);

	public Model save(Model bean);

	public void delete(Model bean);

	// --------------------

	public List<Model> findBySiteIdIn(Collection<Integer> siteIds);

	// @Query("select count(*) from Model bean where bean.site.id in (?1)")
	// public long countBySiteId(Collection<Integer> siteIds);
}
