package com.guild.calendar.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class SuccessCode {
	
	private int stateCode;
	
	private Long id;
	
	private String message;
}
