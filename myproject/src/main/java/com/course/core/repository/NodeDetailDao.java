package com.course.core.repository;

import org.springframework.data.repository.Repository;

import com.course.core.domain.NodeDetail;

/**
 * NodeDetailDao
 * 
 * @author benfang
 * 
 */
public interface NodeDetailDao extends Repository<NodeDetail, Integer> {
	public NodeDetail findOne(Integer id);

	public NodeDetail save(NodeDetail bean);
}
