package com.ggw.quartz.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

@Component
public class TestJob implements Job {

	private Logger logger = Logger.getLogger(getClass());
	
	public void execute(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("Running...");
	}

}
