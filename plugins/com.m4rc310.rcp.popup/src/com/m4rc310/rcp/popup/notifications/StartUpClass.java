package com.m4rc310.rcp.popup.notifications;

//import org.quartz.SchedulerException;

public class StartUpClass {

	public void earlyStartup() {
//		if (PlatformUI.isWorkbenchRunning())
//			NotificationQuartzJobHelper.getInstance().setCurrDisplay(PlatformUI.getWorkbench().getDisplay());
//		try {
//			scheduleNotification();
//		} catch (SchedulerException e) {
//			e.printStackTrace();
//		}
	}

//	public void scheduleNotification() throws SchedulerException {
//		Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
//		JobDetail jobDetail = newJob(InvokeNotificationJob.class).withIdentity("job1", "group1").build();
//
//		Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startNow()
//				.withSchedule(simpleSchedule().withIntervalInSeconds(40).repeatForever()).build();
//
//		scheduler.scheduleJob(jobDetail, trigger);
//	}

}
