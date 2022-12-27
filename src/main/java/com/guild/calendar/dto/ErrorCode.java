package com.guild.calendar.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class ErrorCode {
	private HttpStatus httpStatus;
	
	private int errorCode;
	
	private String message;
	
	public ErrorCode() {};
	
	public ErrorCode(HttpStatus httpStatus,String message) {
		this.errorCode = httpStatus.value();
		this.httpStatus = httpStatus;
		this.message = message;
	};
}
