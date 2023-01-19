package com.guild.calendar.Exception;

public enum ExceptionCode {
	NO_TOKEN("-000","토큰 없음"),
	WRONG_TYPE_TOKEN("-001","잘못된 토큰"),
	UNSUPPORTED_TOKEN("-002","지원되지 않는 토큰"),
	DUPLICATE_EMAIL("-100","사용중인 이메일입니다."),
	ACCOUNT_NOT_MATCH("-101","이메일 또는 비밀번호가 일치하지 않습니다."),
	LOGOUT_TOKEN("-102","로그아웃된 토큰입니다."),
	LOGOUT_TOKEN_TIMEOUT("-103","토큰이 만료되어 로그아웃되었습니다."),
	ILLEGAL_ACCESS_TOKEN("-104","엑세스 토큰 값을 확인해주세요"),
	DECRYPTION_ERROR("-106","암호화에 사용한 키를 확인해주세요. API 서버 점검으로 키 변경이 있을 수 있습니다."),
	
	NO_SUCH_CALENDAR("-190","캘린더를 찾지 못했습니다. 캘린더 ID를 확인해주세요"),
	
	NO_SUCH_GUILD("-150","해당 ID로 조회할 수 없습니다."),
	DUPLICATE_GUILDUUSER("-151","해당 값이 이미 존재합니다."),
	USED_GUILD("-152","사용중인 길드는 삭제할 수 없습니다."),
	
	UNKNOWN_ERROR("-999","알수없는 에러입니다.")
	
	
	
	;
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
