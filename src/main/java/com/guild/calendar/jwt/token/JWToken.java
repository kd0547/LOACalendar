package com.guild.calendar.jwt.token;



public class JWToken {
	
	private String accessToken;
	private String refreshToken;
	private String key;
	
	public JWToken(){}
	public JWToken(String accessToken,String refreshToken,String key) {
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.key = key;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public String getKey() {
		return key;
	}
	
	
	
}
