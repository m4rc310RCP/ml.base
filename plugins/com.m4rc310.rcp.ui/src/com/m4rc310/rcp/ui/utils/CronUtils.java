package com.m4rc310.rcp.ui.utils;

import org.eclipse.e4.core.di.annotations.Creatable;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

@Creatable
public class CronUtils {
	private ThreadPoolTaskScheduler scheduler;

	public ThreadPoolTaskScheduler cron(String cron, Runnable run) {
		try {
			this.scheduler = new ThreadPoolTaskScheduler();
			scheduler.setPoolSize(5);
			scheduler.initialize();
			
			scheduler.schedule(run, new CronTrigger(cron));
			return scheduler;
		} catch (Exception e) {
			throw new UnsupportedOperationException(e);
		}
	}
	
	public void shutdown() {
		scheduler.shutdown();
	}
	
}
