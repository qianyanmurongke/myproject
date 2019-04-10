package com.course.core.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import com.course.common.orm.Limitable;
import com.course.core.domain.OrgTemplate;

public interface OrgTemplateDao extends Repository<OrgTemplate,Integer>{

	
	public Page<OrgTemplate> findAll(Specification<OrgTemplate> spec, Pageable pageable);

	public List<OrgTemplate> findAll(Specification<OrgTemplate> spec, Sort sort);

	public List<OrgTemplate> findAll(Specification<OrgTemplate> spec, Limitable limit);

	public List<OrgTemplate> findAll(Sort sort);

	public OrgTemplate findOne(Integer id);

	public OrgTemplate save(OrgTemplate bean);

	public void delete(OrgTemplate bean);
	
	/**
	 * 通过学校id查询模板信息记录
	 * @param orgId
	 * @return ADD xuewei
	 */
	@Query("from OrgTemplate bean where bean.org.id =?1")
	public List<OrgTemplate> findOrgTemplateListByOrgId(Integer orgId);
	
	@Query("from OrgTemplate bean where bean.org.id =?1 and bean.templateType =?2")
	public OrgTemplate findOrgTemplateByOrgIdAndType(Integer orgId,String templateType);
	
	
}
