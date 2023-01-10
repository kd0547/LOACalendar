package com.guild.calendar.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ShareURL {
	
	private String url;
	
	
	private int expiredTime;
}
