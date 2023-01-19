package com.guild.calendar.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Date;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@SpringBootTest
public class JwtTokenTest {
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	Authentication createAuth() {
		String userName = "test@naver.com";
		String password = passwordEncoder.encode("test1234");
		
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
		
		IpUserDetailsToken ipUserDetailsToken = (IpUserDetailsToken) jwtTokenProvider.generateToken(authentication, IpUserDetailsToken.class);
		
		String ipTokenJson = redisPaserUtil.ObjectToJSON(ipUserDetailsToken);
		
		String accessToken = ipUserDetailsToken.getAccessToken();
		
		String username = jwtTokenProvider.getUserIdFromJWT(accessToken);
		
		IpUserDetailsToken josnIpUserDetails = (IpUserDetailsToken) redisPaserUtil.JsonToObject(ipTokenJson, IpUserDetailsToken.class);
		
		//System.out.println(username);
		//System.out.println("accessKey : "+josnIpUserDetails.getAccessToken());
		//System.out.println("refreshKey : "+josnIpUserDetails.getRefreshToken());
		
		
		//assertThat(username).isEqualTo(authentication.getName());
		
	}
	@Test
	void TokenExpiredTest() {
		
		String accessToken = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0QG5hdmVyLmNvbSIsInJvbGVzIjpbeyJhdXRob3JpdHkiOiJST0xFX1VTRVIifV0sImlhdCI6MTY3MzI1MzA5NSwiZXhwIjoxNjczMjU0ODk1LCJqdGkiOiI4MTBlNjA3Yi03MDc4LTRhYjctYTQ4ZC1lYjBmMzI4MzQ5MGIifQ.7GU5LgWEU2GGsyv1t3vHEn_NPk0BVWejSeLmsGXLIK4";
		
		String bearerDeleteToken = getRefreshJwtFromRequest(accessToken);
		
		System.out.println(bearerDeleteToken);
		
	}
	
	
	
	
	private String getRefreshJwtFromRequest(String request) {
		
		if(!request.startsWith("Bearer ")) {
			throw new IllegalArgumentException("-002");
		}
		
		return request.substring("Bearer ".length());
	}  
	
}
