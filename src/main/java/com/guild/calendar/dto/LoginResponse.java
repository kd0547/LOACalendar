package com.guild.calendar.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponse {
	
	private String docURL;
	
	private String tokenType;
	
	private String accessToken;
	
	//private Long expiresAccessToken;
	
	private String refreshToken;

	//private Long expiresRefreshToken;
	
	public LoginResponse(String accessToken) {
		this(null,accessToken,null);
	}
	
	public LoginResponse(String accessToken,String refreshToken) {
		this(null,accessToken,refreshToken);
	}
	
	
	public LoginResponse(String docURL, String accessToken,String refreshToken) {
		this.docURL = "https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/authentication";
		this.tokenType = "Bearer";
		this.accessToken = "Bearer " + accessToken;
		this.refreshToken = "Bearer " + refreshToken;
	}

}
