package com.course.core.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.course.core.constant.Constants;
import com.course.core.task.InfoPublishTask;

public class InfoPublishJob  implements Job{
	private static final Logger logger = LoggerFactory.getLogger(InfoPublishJob.class);

	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			ApplicationContext appContext = (ApplicationContext) context.getScheduler().getContext()
					.get(Constants.APP_CONTEXT);
			InfoPublishTask infoPublishTask = appContext.getBean(InfoPublishTask.class);

			infoPublishTask.start();

			logger.info("run InfoPublish job ");

		} catch (SchedulerException e) {
			throw new JobExecutionException("Cannot get ApplicationContext", e);
		}

	}

}
