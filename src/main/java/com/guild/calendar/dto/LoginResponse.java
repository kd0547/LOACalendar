package com.guild.calendar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
	
	private String accessToken;
	
	private String refreshToken;

	
	public LoginResponse(String accessToken) {
		this(accessToken,null);
	}
	
	public LoginResponse(String accessToken,String refreshToken) {
		this.accessToken = "Bearer " + accessToken;
		this.refreshToken = "Bearer " + refreshToken;
	}

}
