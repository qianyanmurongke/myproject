package com.course.core.repository.plus;

import java.util.List;

import com.course.common.orm.Limitable;
import com.course.core.domain.Site;

/**
 * SiteDaoPlus
 * 
 * @author benfang
 * 
 */
public interface SiteDaoPlus {
	public List<Site> findByStatus(Integer parentId, String parentNumber, Integer[] status, Limitable limitable);
}
