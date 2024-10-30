package com.guild.calendar.jwt.token;


import lombok.Getter;

@Getter
public class JWToken {
	
	private String accessToken; //인증토큰
	private String refreshToken; //인증토큰 발급용
	private String key;
	
	public JWToken(){}
	public JWToken(String accessToken,String refreshToken,String key) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.key = key;
	}
}
