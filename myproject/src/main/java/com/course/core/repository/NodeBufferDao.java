package com.course.core.repository;

import org.springframework.data.repository.Repository;

import com.course.core.domain.NodeBuffer;

/**
 * NodeBufferDao
 * 
 * @author benfang
 * 
 */
public interface NodeBufferDao extends Repository<NodeBuffer, Integer> {
	public NodeBuffer findOne(Integer id);

	public NodeBuffer save(NodeBuffer bean);
}
