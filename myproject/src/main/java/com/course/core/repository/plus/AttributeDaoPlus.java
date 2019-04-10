package com.course.core.repository.plus;

import java.util.List;

import com.course.core.domain.Attribute;

/**
 * AttributeDaoPlus
 * 
 * @author benfang
 * 
 */
public interface AttributeDaoPlus {
	public List<Attribute> findByNumbers(String[] numbers, Integer[] siteIds);
}
