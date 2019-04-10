package com.course.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.course.common.orm.RowSide;
import com.course.core.domain.ScheduleJob;

public interface ScheduleJobService {
	public Page<ScheduleJob> findAll(Map<String, String[]> params, Pageable pageable);

	public RowSide<ScheduleJob> findSide(Map<String, String[]> params, ScheduleJob bean, Integer position, Sort sort);

	public Page<ScheduleJob> findList(Map<String, String[]> params, Pageable pageable);

	public List<ScheduleJob> findList();

	public ScheduleJob get(Integer id);

	public ScheduleJob save(ScheduleJob bean);

	public ScheduleJob update(ScheduleJob bean);

	public ScheduleJob delete(Integer id);

	public List<ScheduleJob> delete(Integer[] ids);

}
