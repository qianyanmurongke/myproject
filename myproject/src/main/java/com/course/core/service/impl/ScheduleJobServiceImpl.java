package com.course.core.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.course.common.orm.Limitable;
import com.course.common.orm.RowSide;
import com.course.common.orm.SearchFilter;
import com.course.core.domain.ScheduleJob;
import com.course.core.repository.ScheduleJobDao;
import com.course.core.service.ScheduleJobService;

@Service
@Transactional(readOnly = true)
public class ScheduleJobServiceImpl implements ScheduleJobService {
	public Page<ScheduleJob> findAll(Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(params), pageable);
	}

	public RowSide<ScheduleJob> findSide(Map<String, String[]> params, ScheduleJob bean, Integer position, Sort sort) {

		if (position == null) {
			return new RowSide<ScheduleJob>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<ScheduleJob> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);

	}

	public Page<ScheduleJob> findList(Map<String, String[]> params, Pageable pageable) {
		return dao.findAll(spec(params), pageable);
	}

	private Specification<ScheduleJob> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<ScheduleJob> sp = SearchFilter.spec(filters, ScheduleJob.class);
		return sp;
	}

	public List<ScheduleJob> findList() {

		return dao.findAll(new Sort("id"));
	}

	public ScheduleJob get(Integer id) {
		return dao.findOne(id);
	}

	@Transactional
	public ScheduleJob save(ScheduleJob bean) {
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public ScheduleJob update(ScheduleJob bean) {
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public ScheduleJob delete(Integer id) {
		ScheduleJob entity = dao.findOne(id);
		dao.delete(entity);
		return entity;
	}

	@Transactional
	public List<ScheduleJob> delete(Integer[] ids) {
		List<ScheduleJob> beans = new ArrayList<ScheduleJob>();
		for (int i = 0; i < ids.length; i++) {
			beans.add(delete(ids[i]));
		}
		return beans;
	}

	@Autowired
	private ScheduleJobDao dao;
}
