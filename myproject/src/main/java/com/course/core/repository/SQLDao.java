package com.course.core.repository;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.course.common.orm.JpqlBuilder.Parameter;

/**
 * SQLDao
 * 
 * @author benfang
 * 
 */
public interface SQLDao {
	public List<?> query(String sql, Integer maxRows, Integer startRow);

	public List<?> queryByMap(String sql, Integer maxRows, Integer startRow);

	public Page<T> pageQuery(String sql, final Pageable pageable);

	public int update(String sql);
	

	@SuppressWarnings("rawtypes")
	public Page page(String sql, List<Parameter> parameters, Pageable pageable);

	@SuppressWarnings("rawtypes")
	public List list(String sql, List<Parameter> parameters, Sort sort);
}
