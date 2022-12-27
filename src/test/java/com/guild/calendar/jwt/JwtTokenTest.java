package com.guild.calendar.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guild.calendar.common.RedisPaserUtil;
import com.guild.calendar.constant.Role;
import com.guild.calendar.jwt.token.IpUserDetailsToken;

@SpringBootTest
public class JwtTokenTest {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	Authentication createAuth() {
		String userName = "test@naver.com";
		String password = passwordEncoder.encode("test");
		
		UserDetails userDetails = User.builder()
				.username(userName)
				.password(password)
				.roles(Role.USER.toString())
				.build();
		
		return  new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
	}
	
	@Test
	void tokenTest() {
		RedisPaserUtil redisPaserUtil = new RedisPaserUtil(new ObjectMapper());
		Authentication authentication = createAuth();
		
		IpUserDetailsToken ipUserDetailsToken = (IpUserDetailsToken) jwtTokenProvider.generateToken(authentication, "ipUserDetailsToken");
		
		String ipTokenJson = redisPaserUtil.ObjectToJSON(ipUserDetailsToken);
		
		String accessToken = ipUserDetailsToken.getAccessToken();
		
		String username = jwtTokenProvider.getUserIdFromJWT(accessToken, ipUserDetailsToken.getKey());
		
		IpUserDetailsToken josnIpUserDetails = (IpUserDetailsToken) redisPaserUtil.JsonToObject(ipTokenJson, IpUserDetailsToken.class);
		
		
		System.out.println("ipTokenJson : "+ipTokenJson);
		
		
		
		assertThat(username).isEqualTo(authentication.getName());
		
	}
	
	

	
}
