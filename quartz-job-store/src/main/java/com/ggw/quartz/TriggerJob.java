package com.ggw.quartz;

import org.apache.log4j.Logger;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import lombok.Getter;
import lombok.Setter;

public class TriggerJob {

	private static Logger logger = Logger.getLogger("TriggerJob");
	
	private static TriggerJob instance;
	
	@Getter @Setter
	private ApplicationContext cxt;
	
	public static TriggerJob getInstance() {
		if (instance == null) {
			instance = setup();
		}
		return instance;
	}
	
	private static TriggerJob setup() {
		logger.info("Setup TriggerJob...");
		TriggerJob triggerJob = new TriggerJob();
		ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext.xml");
		triggerJob.setCxt(cxt);
		return triggerJob;
	}
	
	public void distory() {
		logger.info("Distory TriggerJob...");
		if (this.cxt instanceof ConfigurableApplicationContext) {
		    ((ConfigurableApplicationContext) this.cxt).close();
		}
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void run() throws SchedulerException {
		Scheduler scheduler = (Scheduler) cxt.getBean("quartzScheduler");
		scheduler.start();
	}
	
	public void stop() throws SchedulerException {
		Scheduler scheduler = (Scheduler) cxt.getBean("quartzScheduler");
		scheduler.shutdown();
	}
}
