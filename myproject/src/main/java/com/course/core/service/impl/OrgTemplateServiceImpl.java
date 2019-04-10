package com.course.core.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.course.common.orm.Limitable;
import com.course.common.orm.RowSide;
import com.course.common.orm.SearchFilter;
import com.course.core.domain.OrgTemplate;
import com.course.core.repository.OrgTemplateDao;
import com.course.core.service.OrgTemplateService;

/**
 * 模板信息
 * @author Administrator
 *
 */
@Service
@Transactional(readOnly=true)
public class OrgTemplateServiceImpl implements OrgTemplateService{
	
	private Specification<OrgTemplate> spec(Map<String, String[]> params) {
		Collection<SearchFilter> filters = SearchFilter.parse(params).values();
		Specification<OrgTemplate> sp = SearchFilter.spec(filters, OrgTemplate.class);
		return sp;
	}

	
	public Page<OrgTemplate> findAll(Map<String, String[]> params, Pageable pageable) {
		
		return dao.findAll(spec(params), pageable);
	}

	@Override
	public List<OrgTemplate> findList(Map<String, String[]> params, Sort sort) {
		
		return dao.findAll(spec(params), sort);
	}

	@Override
	public RowSide<OrgTemplate> findSide(Map<String, String[]> params, OrgTemplate bean, Integer position, Sort sort) {
		if (position == null) {
			return new RowSide<OrgTemplate>();
		}
		Limitable limit = RowSide.limitable(position, sort);
		List<OrgTemplate> list = dao.findAll(spec(params), limit);
		return RowSide.create(list, bean);
	}

	@Override
	public List<OrgTemplate> findList() {
		
		return dao.findAll(new Sort(Direction.ASC, "id"));
	}

	@Override
	public OrgTemplate get(Integer id) {
		
		return dao.findOne(id);
	}

	@Transactional
	public OrgTemplate save(OrgTemplate bean) {
		bean = dao.save(bean);
		return bean;
	}

	@Transactional
	public OrgTemplate update(OrgTemplate bean) {
		bean = dao.save(bean);
		return bean;
	}

	private OrgTemplate doDelete(Integer id) {
		OrgTemplate entity = dao.findOne(id);
		if (entity != null) {
			dao.delete(entity);
		}
		return entity;
	}
	
	@Transactional
	public OrgTemplate delete(Integer id) {
		
		return doDelete(id);
	}
	
	@Transactional
	public OrgTemplate[] delete(Integer[] ids) {
		OrgTemplate[] beans = new OrgTemplate[ids.length];
		for (int i = 0; i < ids.length; i++) {
			beans[i] = delete(ids[i]);
		}
		return beans;
	}
	
	/**
	 * 通过学校id查询模板信息记录
	 * @param orgId
	 * @return ADD xuewei
	 */
	public List<OrgTemplate> findOrgTemplateListByOrgId(Integer orgId) {
		
		return dao.findOrgTemplateListByOrgId(orgId);
	}
	
	
	public OrgTemplate findOrgTemplateByOrgIdAndType(Integer orgId, String templateType) {
		
		return dao.findOrgTemplateByOrgIdAndType(orgId, templateType);
	}


	

	@Autowired
	private OrgTemplateDao dao ;
	
	public void SetDao(OrgTemplateDao dao) {
		this.dao = dao ;
	}



	
}
