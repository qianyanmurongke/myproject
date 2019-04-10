package com.course.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.course.common.orm.Limitable;
import com.course.core.domain.SysDealGroups;

public interface SysDealGroupsDao extends Repository<SysDealGroups,Integer>{

	public Page<SysDealGroups> findAll(Specification<SysDealGroups> spec, Pageable pageable);
	
	public List<SysDealGroups> findAll(Specification<SysDealGroups> spec, Limitable limitable);
	
	public List<SysDealGroups> findAll(Sort sort);

	public SysDealGroups findOne(Integer id);

	public SysDealGroups save(SysDealGroups bean);

	public void delete(SysDealGroups bean);
	
	@Query("from SysDealGroups bean where bean.org.id=?1")
	public List<SysDealGroups> findByOrgId(Integer orgid);

}
