package com.ggw.quartz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/console/quartz")
public class QuartzManager {

	@RequestMapping(value = "/management", method = RequestMethod.GET)
	public ModelAndView mamagement() {
		return new ModelAndView("quartz-management");
	}

}
