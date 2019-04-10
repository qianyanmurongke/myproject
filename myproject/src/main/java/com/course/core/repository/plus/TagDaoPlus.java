package com.course.core.repository.plus;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.course.common.orm.Limitable;
import com.course.core.domain.Tag;

/**
 * TagDaoPlus
 * 
 * @author benfang
 * 
 */
public interface TagDaoPlus {
	public List<Tag> findList(Integer[] siteId, String[] node,
			Integer[] nodeId, Integer refers, Limitable limitable);

	public Page<Tag> findPage(Integer[] siteId, String[] node,
			Integer[] nodeId, Integer refers, Pageable pageable);

	public List<Tag> findByName(String[] names, Integer[] siteIds);
}
