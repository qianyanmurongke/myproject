package com.course.core.repository.plus;

import com.course.common.orm.Limitable;
import com.course.core.domain.Node;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * NodeDaoPlus
 * 
 * @author benfang
 * 
 */
public interface NodeDaoPlus {
	public List<Node> findList(Integer[] siteId, Integer parentId,
			String treeNumber, Boolean isRealNode, Boolean isHidden,Integer[] p0,
			Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4,
			Integer[] p5, Integer[] p6, Limitable limitable);

	public Page<Node> findPage(Integer[] siteId, Integer parentId,
			String treeNumber, Boolean isRealNode, Boolean isHidden,Integer[] p0,
			Integer[] p1, Integer[] p2, Integer[] p3, Integer[] p4,
			Integer[] p5, Integer[] p6, Pageable pageable);

	public List<Node> findByNumbersLike(String[] numbers, Integer[] siteIds);

	public List<Node> findByNumbers(String[] numbers, Integer[] siteIds);

	public List<Node> findForHtml(Integer siteId, Integer nodeId,
			String treeNumber, boolean forUpdate, Integer lastId, int maxResult);
}
