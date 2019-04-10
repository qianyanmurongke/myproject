package com.course.common.quartz;

import java.text.ParseException;
import java.util.List;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.course.core.domain.ScheduleJob;
import com.course.core.service.ScheduleJobService;

public class LoadTask {
	public void initTask() throws SchedulerException, ClassNotFoundException, ParseException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();

		List<ScheduleJob> scheduleJobs = this.scheduleJobService.findList();

		for (ScheduleJob bean : scheduleJobs) {
			if (bean.getStatus() == 1)
				continue;
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(bean.getTriggerKey());
			if (trigger == null) {
				scheduler.scheduleJob(bean.getJobDetail(), bean.getTrigger());
			} else {
				// trigger已存在，则更新相应的定时设置
				CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(bean.getCronExpression());
				// 按新的cronExpression表达式重新构建trigger
				trigger = trigger.getTriggerBuilder().withIdentity(bean.getTriggerKey()).withSchedule(scheduleBuilder)
						.build();
				// 按新的trigger重新设置job执行
				scheduler.rescheduleJob(bean.getTriggerKey(), trigger);
			}
		}
	}
	
	 @Autowired
	 private SchedulerFactoryBean schedulerFactoryBean;


	@Autowired
	private ScheduleJobService scheduleJobService;
}
