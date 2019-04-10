package com.course.core.repository.plus;

import java.util.List;

import com.course.core.domain.Model;

/**
 * ModelDaoPlus
 * 
 * @author benfang
 * 
 */
public interface ModelDaoPlus {
	public List<Model> findList(Integer siteId, String type);

	public Model findDefault(Integer siteId, String type);

	public List<Model> findByNumbers(String[] numbers, Integer[] siteIds);
}
