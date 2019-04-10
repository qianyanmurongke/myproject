package com.course.core.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.course.common.orm.Limitable;
import com.course.common.orm.RowSide;
import com.course.common.orm.SearchFilter;
import com.course.core.domain.SysDealGroups;
import com.course.core.repository.SysDealGroupsDao;
import com.course.core.service.SysDealGroupsService;

@Service
@Transactional(readOnly = true)
public class SysDealGroupsServiceImpl implements SysDealGroupsService {

	private Specification<SysDealGroups> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<SysDealGroups> sp = SearchFilter.spec(filters, SysDealGroups.class);
		return sp;
	}
	
	@Override
	public Page<SysDealGroups> findAll(Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(params), pageable);
	}

	@Override
	public SysDealGroups get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public SysDealGroups save(SysDealGroups bean) {
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public SysDealGroups[] delete(Integer[] ids) {
		SysDealGroups[] beans = new SysDealGroups[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}
	
	@Transactional
	public SysDealGroups delete(Integer id) {
		return doDelete(id);
	}
	
	private SysDealGroups doDelete(Integer id) {
		SysDealGroups entity = dao.findOne(id);
		if (entity != null) {
			dao.delete(entity);
		}
		return entity;
	}
	
	@Override
	public RowSide<SysDealGroups> findSide(Map<String, String[]> params, SysDealGroups bean, Integer position,
			Sort sort) {
		if (position == null) {
			return new RowSide<SysDealGroups>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<SysDealGroups> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);
	}

	@Transactional
	public SysDealGroups update(SysDealGroups bean) {
		bean = dao.save(bean);
		return bean;
	}

	@Override
	public List<SysDealGroups> findList() {
		return dao.findAll(new Sort(Direction.ASC, "id"));
	}
	
	@Override
	public List<SysDealGroups> findByOrgId(Integer orgid) {
		return dao.findByOrgId(orgid);
	}

	@Autowired
	private SysDealGroupsDao dao;
}
