package com.course.core.domain;

// Generated 2013-6-24 15:12:04 by Hibernate Tools 4.0.0

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.google.common.base.Objects;
import com.course.core.domain.InfoTag.InfoTagId;

/**
 * InfoTag
 * 
 * @author benfang
 * 
 */
@Entity
@Table(name = "cms_info_tag")
@IdClass(InfoTagId.class)
public class InfoTag implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public InfoTag() {
	}

	public InfoTag(Info info, Tag tag) {
		this.info = info;
		this.tag = tag;
	}

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_info_id", nullable = false)
	private Info info;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "f_tag_id", nullable = false)
	private Tag tag;

	public Info getInfo() {
		return info;
	}

	public void setInfo(Info info) {
		this.info = info;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof InfoTag)) {
			return false;
		}
		InfoTag that = (InfoTag) o;
		return Objects.equal(info, that.info) && Objects.equal(tag, that.tag);
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(info, tag);
	}

	public static class InfoTagId implements Serializable {
		private static final long serialVersionUID = 1L;

		Integer info;
		Integer tag;

		public InfoTagId() {
		}

		public InfoTagId(Integer info, Integer tag) {
			this.info = info;
			this.tag = tag;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof InfoTagId)) {
				return false;
			}
			InfoTagId that = (InfoTagId) o;
			return Objects.equal(info, that.info) && Objects.equal(tag, that.tag);
		}

		@Override
		public int hashCode() {
			return Objects.hashCode(info, tag);
		}
	}
}
