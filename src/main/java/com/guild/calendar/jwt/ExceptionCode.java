package com.guild.calendar.jwt;

public enum ExceptionCode {
	NO_TOKEN("401-000","토큰 없음"),
	WRONG_TYPE_TOKEN("401-001","잘못된 토큰"),
	UNSUPPORTED_TOKEN("401-002","지원되지 않는 토큰");
	
	private String errorCode;
	private String errorMessage;
	
	ExceptionCode(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	ExceptionCode() {
		
	}

	public String getCode() {
		return this.errorCode;
	}
	public String getMessage() {
		return this.errorMessage;
	}
	
}
