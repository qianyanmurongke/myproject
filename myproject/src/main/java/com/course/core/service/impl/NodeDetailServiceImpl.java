package com.course.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.course.core.domain.Node;
import com.course.core.domain.NodeDetail;
import com.course.core.repository.NodeDetailDao;
import com.course.core.service.NodeDetailService;

/**
 * NodeDetailServiceImpl
 * 
 * @author benfang
 * 
 */
@Service
@Transactional(readOnly = true)
public class NodeDetailServiceImpl implements NodeDetailService {
	@Transactional
	public void save(NodeDetail detail, Node node) {
		node.setDetail(detail);
		detail.setNode(node);
		detail.applyDefaultValue();
		dao.save(detail);
	}

	@Transactional
	public NodeDetail update(NodeDetail detail, Node node) {
		detail.setNode(node);
		detail.applyDefaultValue();
		NodeDetail entity = dao.save(detail);
		node.setDetail(detail);
		return entity;
	}

	private NodeDetailDao dao;

	@Autowired
	public void setDao(NodeDetailDao dao) {
		this.dao = dao;
	}
}
