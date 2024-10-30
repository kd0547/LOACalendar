package com.guild.calendar.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {

	@NotNull
	private String email; //이메일
	private String secret; //비밀번호
}
