package com.course.core.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.Repository;

import com.course.common.orm.Limitable;
import com.course.core.domain.MailInbox;
import com.course.core.repository.plus.MailInboxDaoPlus;

public interface MailInboxDao extends Repository<MailInbox, Integer>, MailInboxDaoPlus {
	public Page<MailInbox> findAll(Specification<MailInbox> spec, Pageable pageable);

	public List<MailInbox> findAll(Specification<MailInbox> spec, Limitable limitable);

	public MailInbox findOne(Integer id);

	public MailInbox save(MailInbox bean);

	public void delete(MailInbox bean);

	// --------------------

	public List<MailInbox> findByReceiverIdIn(Collection<Integer> userIds);
}
