package com.course.core.fulltext;

import com.course.core.service.TaskService;

/**
 * InfoFulltextDao
 * 
 * @author benfang
 * 
 */
public interface InfoFulltextDao {
	public int addDocument(Integer siteId, String treeNumber,
			TaskService taskService, Integer taskId);
}
