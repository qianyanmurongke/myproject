package com.course.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import com.course.common.orm.Limitable;
import com.course.core.domain.ScheduleJob;

public interface ScheduleJobDao extends Repository<ScheduleJob, Integer>{
	public Page<ScheduleJob> findAll(Specification<ScheduleJob> spec, Pageable pageable);

	public List<ScheduleJob> findAll(Specification<ScheduleJob> spec, Sort sort);

	public List<ScheduleJob> findAll(Specification<ScheduleJob> spec, Limitable limit);

	public List<ScheduleJob> findAll(Sort sort);

	public ScheduleJob findOne(Integer id);

	public ScheduleJob save(ScheduleJob bean);

	public void delete(ScheduleJob bean);
}
