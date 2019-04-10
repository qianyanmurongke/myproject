package com.course.core.domain;

import java.util.Date;

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
 * 
 * @Description 刷卡机组
 * @author wangfei
 * @date 2019年3月14日下午2:22:36
 * @version  1.0
 */
@Entity
@Table(name="sys_deal_groups")
public class SysDealGroups implements java.io.Serializable {
	private static final long serialVersionUID=1L;
	
	private Integer id;
	
	/**
	 * 学校id
	 */
	private Org org;
	
	/**
	 * 组名称
	 */
	private String name;
	
	/**
	 * 创建时间
	 */
	private  Date created;
	
	/**
	 * 创建人
	 */
	private User creator;
	
	/**
	 * 修改时间
	 */
	private Date updated;
	
	/**
	 * 修改人
	 */
	private User updator;

	@Id
	@Column(name = "f_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_sys_deal_groups", pkColumnValue = "sys_deal_groups", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_sys_deal_groups")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_org_id")
	public Org getOrg() {
		return org;
	}

	public void setOrg(Org org) {
		this.org = org;
	}

	@Column(name = "f_name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "f_created")
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_creator_id")
	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	@Column(name = "f_updated")
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_updator_id")
	public User getUpdator() {
		return updator;
	}

	public void setUpdator(User updator) {
		this.updator = updator;
	}
	
}
