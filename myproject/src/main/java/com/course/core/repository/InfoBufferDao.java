package com.course.core.repository;

import org.springframework.data.repository.Repository;

import com.course.core.domain.InfoBuffer;

/**
 * InfoBufferDao
 * 
 * @author benfang
 * 
 */
public interface InfoBufferDao extends Repository<InfoBuffer, Integer> {
	public InfoBuffer findOne(Integer id);

	public InfoBuffer save(InfoBuffer bean);
}
