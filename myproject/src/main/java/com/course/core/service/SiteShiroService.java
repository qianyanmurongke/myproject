package com.course.core.service;

import java.util.List;

import com.course.core.domain.Site;

/**
 * SiteService
 * 
 * @author benfang
 * 
 */
public interface SiteShiroService {
	public List<Site> findByUserId(Integer userId);

	public Site findByDomain(String domain);

	public Site get(Integer id);
}
