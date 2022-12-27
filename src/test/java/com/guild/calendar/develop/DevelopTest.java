package com.guild.calendar.develop;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.guild.calendar.jwt.token.IpUserDetailsToken;
import com.guild.calendar.jwt.token.JWToken;
import com.guild.calendar.jwt.token.UserDetailsToken;

public class DevelopTest {
	
	@Test
	void typeTest() {
		
		System.out.println(isIpUserDetailsToken(IpUserDetailsToken.class));
		

		
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
