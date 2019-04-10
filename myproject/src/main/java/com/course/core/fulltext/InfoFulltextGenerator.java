package com.course.core.fulltext;

import com.course.core.domain.Info;
import com.course.core.domain.Node;

public interface InfoFulltextGenerator {

	public void addDocument(Info[] beans);

	public void updateDocument(Info[] beans);

	public void deleteDocuments(Info[] beans);

	public void addDocument(Integer siteId, final Node node, Integer userId);
}
