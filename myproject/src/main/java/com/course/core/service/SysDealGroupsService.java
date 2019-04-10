package com.course.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.course.common.orm.RowSide;
import com.course.core.domain.SysDealGroups;

public interface SysDealGroupsService {

	public Page<SysDealGroups> findAll(Map<String, String[]> params, Pageable pageable);

	public SysDealGroups get(Integer id);

	public SysDealGroups save(SysDealGroups bean);	

	public SysDealGroups[] delete(Integer[] ids);

	public RowSide<SysDealGroups> findSide(Map<String, String[]> params, SysDealGroups bean, Integer position, Sort sort);

	public SysDealGroups update(SysDealGroups bean);
	
	public List<SysDealGroups> findList();
	
	public List<SysDealGroups> findByOrgId(Integer orgid);
}
