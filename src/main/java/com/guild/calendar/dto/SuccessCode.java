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
	public SuccessCode() {}
	
	public SuccessCode(int stateCode,String message) {
		this(stateCode,null,message);
	}
	
	public SuccessCode(int stateCode,Long id) {
		this(stateCode,id,null);
	}
	
	public SuccessCode(int stateCode,Long id,String message) {
		this.stateCode = stateCode;
		this.id = id;
		this.message = message;
	}
}
