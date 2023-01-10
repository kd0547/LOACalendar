package com.guild.calendar.redis;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guild.calendar.common.RedisPaserUtil;
import com.guild.calendar.constant.Role;
import com.guild.calendar.jwt.JwtTokenProvider;
import com.guild.calendar.jwt.token.IpUserDetailsToken;
import com.guild.calendar.jwt.token.JWToken;

@SpringBootTest
public class RedisTest {
	
	
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	private JWTokenRedisService jwTokenRedisService;
	
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
	void testString() {
		RedisPaserUtil redisPaserUtil = new RedisPaserUtil(new ObjectMapper());
		Authentication authentication = createAuth();
		
		IpUserDetailsToken ipUserDetailsToken = (IpUserDetailsToken) jwtTokenProvider.generateToken(authentication,IpUserDetailsToken.class);
		
		String accessToken_key = ipUserDetailsToken.getAccessToken();
	
		
		redisService.setData(authentication.getName(), ipUserDetailsToken);
		
		//요청이 들어오면 
		String accessKey = jwtTokenProvider.getUserIdFromJWT(accessToken_key);
		
		
		IpUserDetailsToken findToken = (IpUserDetailsToken) redisService.getData(accessKey, IpUserDetailsToken.class);
		
		
		System.out.println(accessKey);
		System.out.println("accessKey : "+findToken.getAccessToken());
		System.out.println("refreshKey : "+findToken.getRefreshToken());
		
		
		
		
		
		
	}
	/*
	@Test
	void testString() {
		RedisPaserUtil redisPaserUtil = new RedisPaserUtil(new ObjectMapper());
		Authentication authentication = createAuth();
		
		IpUserDetailsToken ipUserDetailsToken = (IpUserDetailsToken) jwtTokenProvider.generateToken(authentication, "ipUserDetailsToken");
		
		String accessToken_key = ipUserDetailsToken.getAccessToken();
		String ipUserDetailsToken_value = redisPaserUtil.ObjectToJSON(ipUserDetailsToken);
		
		redisService.setDate(accessToken_key, ipUserDetailsToken_value);
		
		//요청이 들어오면 
		String request_AccessToken_Key = accessToken_key;
		
		IpUserDetailsToken findToken = (IpUserDetailsToken) redisService.getDate(request_AccessToken_Key, IpUserDetailsToken.class);
		
		String findAccessToken = findToken.getAccessToken();
		String findKey = findToken.getKey();
		
		
		
		String findUsername = jwtTokenProvider.getUserIdFromJWT(findAccessToken, findKey);
		
		System.out.println(findUsername);
		assertThat(findUsername).isEqualTo(authentication.getName());
		
	}
	*/
}
