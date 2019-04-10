package com.course.core.service.impl;

import java.util.List;

import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.course.common.orm.JpqlBuilder.Parameter;
import com.course.core.repository.SQLDao;
import com.course.core.service.SQLService;

/**
 * SQLServiceImpl
 * 
 * @author benfang
 * 
 */
@Service
@Transactional(readOnly = true)
public class SQLServiceImpl implements SQLService {
	public List<?> query(String sql, Integer maxRows, Integer startRow) {
		return dao.query(sql, maxRows, startRow);
	}

	public List<?> queryByMap(String sql, Integer maxRows, Integer startRow) {
		return this.dao.queryByMap(sql, maxRows, startRow);
	}

	public Page<T> pageQuery(String sql, final Pageable pageable) {
		return this.dao.pageQuery(sql, pageable);
	}

	public int update(String sql) {
		return dao.update(sql);
	}

	@SuppressWarnings("rawtypes")
	public Page page(String sql, List<Parameter> parameters, Pageable pageable) {
		return this.dao.page(sql, parameters, pageable);
	}

	@SuppressWarnings("rawtypes")
	public List list(String sql, List<Parameter> parameters, Sort sort) {
		return this.dao.list(sql, parameters, sort);
	}

	private SQLDao dao;

	@Autowired
	public void setDao(SQLDao dao) {
		this.dao = dao;
	}
}
