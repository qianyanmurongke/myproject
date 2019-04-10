package com.course.core.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

/**
 * 公众号信息模板
 * @author Administrator
 *
 */
@Entity
@Table(name = "cms_org_template")
public class OrgTemplate implements java.io.Serializable {

	/**
	 * 编号
	 */
	private Integer id;
	
	/**
	 * 学校
	 */
	private Org org;
	
	/**
	 * 模板编号
	 */
	private String code;
	
	/**
	 * 模板Id
	 */
	private String template;
	
	/**
	 * 模板类型
	 */
	private String templateType;
	


	@Id
	@Column(name = "f_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_cms_org_template", pkColumnValue = "cms_org_template", initialValue = 1, allocationSize = 10)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_cms_org_template")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name = "f_org_id")
	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	@Column(name = "f_code")
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "f_template")
	public String getTemplate() {
		return template;
	}

	public void setTemplate(String template) {
		this.template = template;
	}

	@Column(name = "f_template_type")
	public String getTemplateType() {
		return templateType;
	}

	public void setTemplateType(String templateType) {
		this.templateType = templateType;
	}
	
	
	
	
}
