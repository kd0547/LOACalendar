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
	
	private String errorCodeDetail;
	
	private String message;
	
	public ErrorCode() {};
	
	public ErrorCode(HttpStatus httpStatus) {
		this(httpStatus,null,null);
	};
	
	public ErrorCode(HttpStatus httpStatus,String message) {
		this(httpStatus,null,message);
	};
	
	
	
	/*
	 *	2023-01-05 추가
	 * 	에러코드의 상세내용이 필용해 추가했습니다. 
	 * 	
	 */	
	public ErrorCode(HttpStatus httpStatus,String errorCodeDetail,String message) {
		this.errorCode = httpStatus.value();
		this.errorCodeDetail = errorCodeDetail;
		this.httpStatus = httpStatus;
		this.message = message;
	};
}
