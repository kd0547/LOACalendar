package com.guild.calendar.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guild.calendar.dto.ErrorCode;
import com.guild.calendar.dto.LoginRequest;
import com.guild.calendar.dto.LoginResponse;
import com.guild.calendar.dto.UserDetail;
import com.guild.calendar.entity.Member;
import com.guild.calendar.jwt.CustomTokenProvider;
import com.guild.calendar.jwt.JwtTokenProvider;
import com.guild.calendar.jwt.token.IpUserDetailsToken;
import com.guild.calendar.redis.RedisService;
import com.guild.calendar.service.MemberService;


@Controller
@RequestMapping("/auth")
public class AuthController {
	
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private CustomTokenProvider customTokenProvider;
	
	@Autowired
	private RedisService redisService;
	
	
	@GetMapping("/before") 
	public ResponseEntity<?> loginBefore(HttpServletRequest request){
		
		
		
		return null;
	}
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		
		try {
			UserDetail findMember = memberService.findByEmail(loginRequest);
			
			
			UserDetails userDetails = findMember.convertUserDetails();
			System.out.println(userDetails.getUsername());
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
			
			IpUserDetailsToken ipUserDetailToken = (IpUserDetailsToken) customTokenProvider.generateToken(authentication, "ipUserDetailsToken");
			ipUserDetailToken.setUserDetail(findMember);
			
			redisService.setData(ipUserDetailToken.getAccessToken(), ipUserDetailToken);
			
			//redis 오류 대비 Email, AccessKey,RefreshKey,secretKey 로 DB 저장 구현
			
			return ResponseEntity.ok(new LoginResponse(ipUserDetailToken.getAccessToken()));
			
		} catch (UsernameNotFoundException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorCode(HttpStatus.BAD_REQUEST,e.getMessage())) ;
			
		} catch (IllegalArgumentException ie) {
			ie.printStackTrace();
			return ResponseEntity.badRequest().body(new ErrorCode(HttpStatus.BAD_REQUEST,ie.getMessage()));
		}
	}
	 
	
}
