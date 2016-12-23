package com.ggw.quartz.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ggw.quartz.dto.JobDto;

@Service
public class QuartzManagerService {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private Scheduler scheduler;
	
	private JobKey getJobKeyByName(String jobName) throws SchedulerException {
		for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.anyJobGroup())) {
			if (jobName.equals(jobKey.getName())) {
				return jobKey;
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<JobDto> getAllJobs() {
		List<JobDto> jobs = new ArrayList<JobDto>();
		try {
			for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.anyJobGroup())) {
				JobDto jobDto = new JobDto();
				jobDto.setName(jobKey.getName());
				
				List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
				for (Trigger trigger : triggers) {
					jobDto.setState(scheduler.getTriggerState(trigger.getKey()).name());
					jobDto.setLastExecuteTime(trigger.getPreviousFireTime());
					jobDto.setNextExecuteTime(trigger.getNextFireTime());
					jobDto.setDescription(trigger.getDescription());
				}
				
				jobs.add(jobDto);
				
			}
		} catch (SchedulerException e) {
			logger.warn(e);
		}
		return jobs;
	}
	
	public boolean pauseJobByName(String jobName) {
		boolean result = false;
		try {
			JobKey jobKey = getJobKeyByName(jobName);
			if (jobKey != null) {
				scheduler.pauseJob(jobKey);
				logger.info("Pause Job [" + jobName + "] Success.");
				result = true;
			}
		} catch (SchedulerException e) {
			logger.warn("Pause Job [" + jobName + "] Error.", e);
			result = false;
		}
		return result;
	}
	
	public boolean resumeJobByName(String jobName) {
		boolean result = false;
		try {
			JobKey jobKey = getJobKeyByName(jobName);
			if (jobKey != null) {
				scheduler.resumeJob(jobKey);
				logger.info("Resume Job [" + jobName + "] Success.");
				result = true;
			}
		} catch (SchedulerException e) {
			logger.warn("Resume Job [" + jobName + "] Error.", e);
			result = false;
		}
		return result;
	}
	
}