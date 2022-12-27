package com.guild.calendar.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class MemberForm {
	
	private Long id;
	private String email;
	private String username;
	private String password;
	
}
