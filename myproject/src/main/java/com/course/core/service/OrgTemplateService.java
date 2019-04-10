package com.course.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.course.common.orm.RowSide;
import com.course.core.domain.OrgTemplate;

public interface OrgTemplateService {


	public Page<OrgTemplate> findAll(Map<String, String[]> params, Pageable pageable);

	public List<OrgTemplate> findList(Map<String, String[]> params, Sort sort);

	public RowSide<OrgTemplate> findSide(Map<String, String[]> params, OrgTemplate bean, Integer position, Sort sort);

	public List<OrgTemplate> findList();

	public OrgTemplate get(Integer id);

	public OrgTemplate save(OrgTemplate bean);

	public OrgTemplate update(OrgTemplate bean);

	public OrgTemplate delete(Integer id);
	
	public OrgTemplate[] delete(Integer[] ids);
	
	public List<OrgTemplate> findOrgTemplateListByOrgId(Integer orgId);
	

	public OrgTemplate findOrgTemplateByOrgIdAndType(Integer orgId,String templateType);
}
