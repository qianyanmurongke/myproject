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



@Entity
@Table(name = "sys_deal")
public class SysDeal implements java.io.Serializable{
	private static final long serialVersionUID=1L;
	
	
	private Integer id;
	
	/**
	 * 刷卡组
	 */
	private SysDealGroups sysdealgroups;
	
	/**
	 * 名称
	 */
	private String name;
	
	/**
	 * 创建时间
	 */
	private  Date created;

	@Id
	@Column(name = "f_id", unique = true, nullable = false)
	@TableGenerator(name = "tg_sys_deal", pkColumnValue = "sys_deal", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "tg_sys_deal")
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_group_id")
	public SysDealGroups getSysdealgroups() {
		return sysdealgroups;
	}

	public void setSysdealgroups(SysDealGroups sysdealgroups) {
		this.sysdealgroups = sysdealgroups;
	}

	@Column(name = "f_deal_name")
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
	
	
}
