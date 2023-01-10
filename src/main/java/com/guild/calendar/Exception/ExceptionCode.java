package com.guild.calendar.Exception;

public enum ExceptionCode {
	NO_TOKEN("-000","토큰 없음"),
	WRONG_TYPE_TOKEN("-001","잘못된 토큰"),
	UNSUPPORTED_TOKEN("-002","지원되지 않는 토큰"),
	DUPLICATE_EMAIL("-100","사용중인 이메일입니다."),
	ACCOUNT_NOT_MATCH("-101","이메일 또는 비밀번호가 일치하지 않습니다."),
	LOGOUT_TOKEN("-102","로그아웃된 토큰입니다."),
	LOGOUT_TOKEN_TIMEOUT("-103","만료된 토큰입니다."),
	
	UNKNOWN_ERROR("-999","알수없는 에러입니다.");
	
	
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
