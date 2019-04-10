package com.course.core.repository;

import org.springframework.data.repository.Repository;

import com.course.core.domain.InfoDetail;

/**
 * InfoDetailDao
 * 
 * @author benfang
 * 
 */
public interface InfoDetailDao extends Repository<InfoDetail, Integer> {
	public InfoDetail findOne(Integer id);

	public InfoDetail save(InfoDetail bean);

}
