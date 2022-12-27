package com.guild.calendar.jwt.token;



import com.guild.calendar.dto.UserDetail;


public class UserDetailsToken extends JWToken{

	private UserDetail userDetail;
	
	
	public UserDetailsToken() {
		super();
	}
	public UserDetailsToken(String accessToken, String refreshToken, String key) {
		
		
		this(null,accessToken,refreshToken,key);
	}
	
	public UserDetailsToken(UserDetail userDetail,String accessToken, String refreshToken, String key) {
		super(accessToken, refreshToken, key);
		
		this.userDetail = userDetail;
		
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	
	

}
