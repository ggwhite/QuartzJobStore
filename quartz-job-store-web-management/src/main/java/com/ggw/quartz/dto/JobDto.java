package com.ggw.quartz.dto;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class JobDto implements Serializable {

	private static final long serialVersionUID = 8064780828502131646L;
	
	private String name;
	private String description;
	
	private String state;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date lastExecuteTime;
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
	private Date nextExecuteTime;
	
}
