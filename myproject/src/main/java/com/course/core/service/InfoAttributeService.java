package com.course.core.service;

import java.util.Map;

import com.course.core.domain.Info;

/**
 * InfoAttributeService
 * 
 * @author benfang
 * 
 */
public interface InfoAttributeService {
	public void update(Info info, Integer[] attrIds, Map<String, String> attrImages);

	public int deleteByAttributeId(Integer attributeId);
}
