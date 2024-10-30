package com.guild.calendar.dto;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @Setter @Getter
public class UserDetail {

	private String username;
	private String password;
	private String role;

	public static class Builder {
		private String username;
		private String password;
		private String role;
		
		public Builder username(String username){this.username = username;return this;}
		public Builder password(String passowrd){this.password = passowrd;return this;}
		public Builder role(String role){this.role = role;return this;}
		public UserDetail build(){return new UserDetail(this);}
	}
	
	public UserDetail() {}
	public UserDetail(Builder builder) {
		this.username = builder.username;
		this.password = builder.password;
		this.role = builder.role;
	}
	
	public static Builder builder() {
		return new Builder();
	}
	
	public UserDetails convertUserDetails() {
		
		return User.builder()
				.username(this.username)
				.password(this.password)
				.roles(this.role)
				.build();
	}
	
}
