package com.guild.calendar.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class SignupRequest {
	
	private String email;
	private String password;
	private String username;
	
}
