package com.guild.calendar.develop;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.guild.calendar.Exception.ExceptionCode;
import com.guild.calendar.jwt.token.IpUserDetailsToken;
import com.guild.calendar.jwt.token.JWToken;
import com.guild.calendar.jwt.token.UserDetailsToken;
import com.guild.calendar.security.DesEncryption;

@SpringBootTest
public class DevelopTest {
	
	@Autowired
	DesEncryption desEncryption;
	
	
	@Test
	void typeTest() {
		
		System.out.println(ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getCode());
	 	
	}
	





	
	
	private boolean isUserDetailsToken(Class classType) {
		
		String type = classType.getName();
		
		return UserDetailsToken.class.getName().equals(type);
	}
	
	private boolean isIpUserDetailsToken(Class classType) {
		String type = classType.getName();
		
		return IpUserDetailsToken.class.getName().equals(type);
	}
}
