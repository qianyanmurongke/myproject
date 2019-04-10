package com.course.core.fulltext;

import java.util.Date;
import java.util.List;

import org.apache.lucene.search.Sort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.course.common.orm.Limitable;
import com.course.core.domain.Info;
import com.course.core.domain.Node;
import com.course.core.service.TaskService;

/**
 * InfoFulltextService
 * 
 * @author benfang
 * 
 */
public interface InfoFulltextService {
	public List<Info> list(Integer[] siteIds, Integer[] nodeIds,
			Integer[] attrIds, Date beginDate, Date endDate, String[] status,
			Integer[] excludeId, String q, String title, String[] keywords,
			String description, String text, String[] creators,
			String[] authors, Integer fragmentSize, Limitable limitable,
			Sort sort);

	public Page<Info> page(Integer[] siteIds, Integer[] nodeIds,
			Integer[] attrIds, Date beginDate, Date endDate, String[] status,
			Integer[] excludeId, String q, String title, String[] keywords,
			String description, String text, String[] creators,
			String[] authors, Integer fragmentSize, Pageable pageable, Sort sort);

	public void addDocument(Integer infoId);

	public void updateDocument(Integer infoId);

	public int addDocument(Integer siteId, Node node, TaskService taskService,
			Integer taskId);
}
