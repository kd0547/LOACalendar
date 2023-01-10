package com.guild.calendar.controller;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.guild.calendar.Exception.ExceptionCode;
import com.guild.calendar.dto.ErrorCode;
import com.guild.calendar.dto.LoginRequest;
import com.guild.calendar.dto.LoginResponse;
import com.guild.calendar.dto.MemberForm;
import com.guild.calendar.dto.SignupRequest;
import com.guild.calendar.dto.SuccessCode;
import com.guild.calendar.dto.UserDetail;
import com.guild.calendar.entity.Member;
import com.guild.calendar.jwt.CustomTokenProvider;
import com.guild.calendar.jwt.JwtTokenProvider;
import com.guild.calendar.jwt.token.IpUserDetailsToken;
import com.guild.calendar.redis.RedisService;
import com.guild.calendar.service.MemberService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;


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
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody MemberForm memberForm) { 
		
		
		memberService.duplicateEmail(memberForm);
		
		//이메일 인증 추가하기 
		
		
		//회원가입 
		//memberService.saveMember(memberForm);
	
		return ResponseEntity.status(HttpStatus.OK).body(new SuccessCode(HttpStatus.OK.value(),"회원가입 완료"));
	}
	
	
	public boolean haveUsernameRedis(String username) {
		boolean haveData= redisService.deleteData(username);
		if(!haveData) {
			throw new IllegalStateException("-102");
		}
		
		return haveData;
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		
		try {
			String accessToken = getJwtFromRequest(request);
			
			customTokenProvider.validateToken(accessToken);
			
			String username = customTokenProvider.getUserIdFromJWT(accessToken);
			
			haveUsernameRedis(username);
			
			
		} catch(ExpiredJwtException eje) {
			String username = eje.getClaims().getSubject();
			
			redisService.deleteData(username);
			
			String code = ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getCode();
			String message = ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getMessage();
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCode(HttpStatus.UNAUTHORIZED, code, message));
		} catch (UnsupportedJwtException se) {
			se.printStackTrace();
				
		} catch (SignatureException | MalformedJwtException mje) {
			mje.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	@PostMapping("/refresh") 
	public ResponseEntity<?> createRefreshToken(HttpServletRequest request) {
		
		
		try {
			//String accessToken = getJwtFromRequest(request);
		
			String refreshToken = getRefreshJwtFromRequest(request);
			customTokenProvider.validateToken(refreshToken);
			String username = customTokenProvider.getUserIdFromJWT(refreshToken);

			//redis에서 유저 데이터를 조회한다.
			IpUserDetailsToken findIpUserDetailsToken = (IpUserDetailsToken) customTokenProvider.findAccessToken(username,IpUserDetailsToken.class);
			
			UserDetails userDetails = findIpUserDetailsToken.getUserDetail().convertUserDetails();
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
			IpUserDetailsToken ipUserDetailToken = (IpUserDetailsToken) customTokenProvider.generateToken(authentication, IpUserDetailsToken.class);
			ipUserDetailToken.setUserDetail(findIpUserDetailsToken.getUserDetail());
			
			System.out.println(ipUserDetailToken.getAccessToken());
			System.out.println(ipUserDetailToken.getRefreshToken());
			System.out.println(ipUserDetailToken.getUserDetail());
			redisService.deleteData(username);
			redisService.setData(authentication.getName(), ipUserDetailToken);
			LoginResponse token = new LoginResponse(ipUserDetailToken.getAccessToken(),ipUserDetailToken.getRefreshToken());
			
			return ResponseEntity.status(HttpStatus.OK).body(token);
		} catch(ExpiredJwtException eje) {
			//리프레쉬 토큰 유효기간 만료
			String username = eje.getClaims().getSubject();
			
			redisService.deleteData(username);
			
			String code = ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getCode();
			String message = ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getMessage();
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCode(HttpStatus.UNAUTHORIZED, code, message));
		} catch (UnsupportedJwtException se) {
			se.printStackTrace();
				
		} catch (SignatureException | MalformedJwtException mje) {
			mje.printStackTrace();		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		return null;
	}
	
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		
		UserDetail findMember = memberService.findByEmail(loginRequest);
		
		UserDetails userDetails = findMember.convertUserDetails();
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
		
		IpUserDetailsToken ipUserDetailToken = (IpUserDetailsToken) customTokenProvider.generateToken(authentication, IpUserDetailsToken.class);
		ipUserDetailToken.setUserDetail(findMember);
		
		redisService.setData(authentication.getName(), ipUserDetailToken);
		
		//redis 오류 대비 Email, AccessKey,RefreshKey,secretKey 로 DB 저장 구현하기
		
		
		LoginResponse token = new LoginResponse(ipUserDetailToken.getAccessToken(),ipUserDetailToken.getRefreshToken());
		
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}
	
	/*
	 * 
	 */

	/**
	 * 
	 * @param exception
	 * @return
	 */
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorCode> handleNullPointerException(NullPointerException exception) {
		String errorMessage = exception.getMessage();
		
		if(errorMessage.equals("-000")) {
			String code = ExceptionCode.NO_TOKEN.getCode();
			String message = ExceptionCode.NO_TOKEN.getMessage();
		
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCode(HttpStatus.UNAUTHORIZED, code, message));
		}
		return null;	
	}
	
	@Deprecated
	@ExceptionHandler(ExpiredJwtException.class)
	public ResponseEntity<ErrorCode> handleExpiredJwtException(ExpiredJwtException exception) {
		String username = exception.getClaims().getSubject();
		System.out.println(username);
		//haveUsernameRedis(username);
		
		String code = ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getCode();
		String message = ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getMessage();
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCode(HttpStatus.UNAUTHORIZED, code, message));
	}
	
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorCode> handleIllegalStateException(IllegalStateException exception) {
		String errorMessage = exception.getMessage();
		
		if(errorMessage.equals("-100")) {
			
			String code = ExceptionCode.DUPLICATE_EMAIL.getCode();
			String message = ExceptionCode.DUPLICATE_EMAIL.getMessage();
			ErrorCode errorCode = new  ErrorCode(HttpStatus.BAD_REQUEST, code, message);
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCode);
		} 
		
		if(errorMessage.equals("-102")) {
			//로그아웃 처리된 토큰 
			String code = ExceptionCode.LOGOUT_TOKEN.getCode();
			String message = ExceptionCode.LOGOUT_TOKEN.getMessage();
			ErrorCode errorCode =new ErrorCode(HttpStatus.UNAUTHORIZED, code, message);
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorCode);
		}
		
		
		return null;
		
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorCode> handleIllegalArgumentException(IllegalArgumentException exception) {
		String errorMessage = exception.getMessage();
		ErrorCode errorCode = new  ErrorCode(HttpStatus.UNAUTHORIZED);
		
		if(errorMessage.equals("-002")) {
			String code = ExceptionCode.UNSUPPORTED_TOKEN.getCode();
			String message = ExceptionCode.UNSUPPORTED_TOKEN.getMessage();
			
			errorCode.setErrorCodeDetail(code);
			errorCode.setMessage(message);
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorCode);
		}
		
		
		if(errorMessage.equals("-101")) {
			String code = ExceptionCode.ACCOUNT_NOT_MATCH.getCode();
			String message = ExceptionCode.ACCOUNT_NOT_MATCH.getMessage();
			
			errorCode.setErrorCodeDetail(code);
			errorCode.setMessage(message);
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorCode);
		}
		String code = ExceptionCode.UNKNOWN_ERROR.getCode();
		String message = ExceptionCode.UNKNOWN_ERROR.getMessage();
		
		errorCode.setErrorCodeDetail(code);
		errorCode.setMessage(message);
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorCode);
		
	}
	
	

	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ErrorCode> handleUsernameNotFoundException(UsernameNotFoundException exception) {
		String errorMessage = exception.getMessage();
		ErrorCode errorCode = new  ErrorCode(HttpStatus.UNAUTHORIZED);
		
		if(errorMessage.equals("-101")) {
			String code = ExceptionCode.ACCOUNT_NOT_MATCH.getCode();
			String message = ExceptionCode.ACCOUNT_NOT_MATCH.getMessage();
			
			errorCode.setErrorCodeDetail(code);
			errorCode.setMessage(message);
		}
		
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorCode);
	}
	
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		if(bearerToken == null) {
			throw new NullPointerException("-000");
		}
		
		if(!bearerToken.startsWith("Bearer ")) {
			throw new IllegalArgumentException("-002");
		}
		
		
		return bearerToken.substring("Bearer ".length());
	}
	private String getRefreshJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization-Refresh");
		
		if(bearerToken == null) {
			throw new NullPointerException("-000");
		}
		
		if(!bearerToken.startsWith("Bearer ")) {
			throw new IllegalArgumentException("-002");
		}
		
		
		return bearerToken.substring("Bearer ".length());
	}  
}
