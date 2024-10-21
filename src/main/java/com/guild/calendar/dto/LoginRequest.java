package com.guild.calendar.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Data
@ToString @Getter @Setter
public class LoginRequest {

	private String email;
	private String secret;
}
