package com.ggw.quartz.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ggw.quartz.dto.JobDto;
import com.ggw.quartz.service.QuartzManagerService;

@RestController
@RequestMapping(value="/api/quartz")
public class QuartzApi {
	
	private Logger logger = Logger.getLogger(getClass());
	
	@Autowired
	private QuartzManagerService service;
	
	@RequestMapping(value="/list", method=RequestMethod.GET)
	public @ResponseBody List<JobDto> list() {
		return service.getAllJobs();
	}
	
	@RequestMapping(value="/pause/{jobName}", method=RequestMethod.GET)
	public @ResponseBody void pauseJob(@PathVariable("jobName") String jobName) {
		logger.info("Pause Job ["+jobName+"]");
		service.pauseJobByName(jobName);
	}
	
	@RequestMapping(value="/resume/{jobName}", method=RequestMethod.GET)
	public @ResponseBody void resumeJob(@PathVariable("jobName") String jobName) {
		logger.info("Resume Job ["+jobName+"]");
		service.resumeJobByName(jobName);
	}
}
