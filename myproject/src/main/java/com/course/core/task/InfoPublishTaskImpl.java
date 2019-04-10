package com.course.core.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.course.core.domain.Site;
import com.course.core.service.InfoService;
import com.course.core.service.SiteService;

public class InfoPublishTaskImpl implements InfoPublishTask {
	private Logger logger = LoggerFactory.getLogger(InfoPublishTaskImpl.class);

	@Override
	public void start() {
		new InfoPublishTaskThread().start();

	}

	public class InfoPublishTaskThread extends Thread {
		public InfoPublishTaskThread() {
		
		}

		@Override
		public void run() {
			try {
				List<Site> sites = siteService.findList();
				for (Site site : sites) {
					Integer publish = infoService.publish(site.getId());
					publish = publish == null ? 0 : publish;
					infoService.tobePublish(site.getId());
					Integer expired = infoService.expired(site.getId());
					expired = expired == null ? 0 : expired;

				}
			} catch (Exception e) {
				logger.error("", e);
			}
		}
	}

	private InfoService infoService;

	private SiteService siteService;

	
	@Autowired
	public void setInfoService(InfoService infoService) {
		this.infoService = infoService;
	}

	@Autowired
	public void setSiteService(SiteService siteService) {
		this.siteService = siteService;
	}



}
