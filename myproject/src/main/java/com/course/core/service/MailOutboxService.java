package com.course.core.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.course.common.orm.RowSide;
import com.course.core.domain.MailOutbox;
import com.course.core.domain.MailText;
import com.course.core.domain.User;

public interface MailOutboxService {
	public Page<MailOutbox> findAll(Map<String, String[]> params, Pageable pageable);

	public RowSide<MailOutbox> findSide(Map<String, String[]> params, MailOutbox bean, Integer position, Sort sort);

	public MailOutbox send(String receiverUsername, Integer[] receiverGroupIds, boolean allReceive, User sender,
			MailText mailText);

	public MailOutbox get(Integer id);

	public MailOutbox save(MailOutbox bean);

	public MailOutbox update(MailOutbox bean);

	public MailOutbox delete(Integer id);

	public List<MailOutbox> delete(Integer[] ids);
}
