package com.course.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.course.common.orm.Limitable;
import com.course.core.domain.Tag;
import com.course.core.repository.plus.TagDaoPlus;

/**
 * TagDao
 * 
 * @author benfang
 * 
 */
public interface TagDao extends Repository<Tag, Integer>, TagDaoPlus {
	public Page<Tag> findAll(Specification<Tag> spec, Pageable pageable);

	public List<Tag> findAll(Specification<Tag> spec, Limitable limitable);

	public Tag findOne(Integer id);

	public Tag save(Tag bean);

	public void delete(Tag bean);

	// --------------------

	public List<Tag> findBySiteIdAndName(Integer siteId, String name);

	@Query("select count(*) from Tag bean where bean.site.id in ?1")
	public long countBySiteId(Collection<Integer> siteIds);
}
