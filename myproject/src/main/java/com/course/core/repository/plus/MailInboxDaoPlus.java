package com.course.core.repository.plus;

import com.course.core.domain.MailOutbox;

public interface MailInboxDaoPlus {
	public int allReceive(MailOutbox outbox);

	public int groupReceiveGroup(MailOutbox outbox, Integer[] groupIds);
}
