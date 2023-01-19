package com.guild.calendar.dto;

import java.security.PublicKey;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class LoginEncryption {
	
	private String Algorithm;
	
	private byte[] Encoded;
	
	private String format;
}
