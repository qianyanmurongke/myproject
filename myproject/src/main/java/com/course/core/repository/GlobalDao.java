package com.course.core.repository;

import org.springframework.data.repository.Repository;

import com.course.core.domain.Global;

/**
 * GlobalDao
 * 
 * @author benfang
 * 
 */
public interface GlobalDao extends Repository<Global, Integer> {
	public Global findOne(Integer id);

	public Global save(Global entity);
}