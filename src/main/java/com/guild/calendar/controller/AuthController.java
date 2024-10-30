package com.guild.calendar.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.util.Base64;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import com.guild.calendar.Exception.ExceptionCode;
import com.guild.calendar.dto.ErrorCode;
import com.guild.calendar.dto.IsPublicKey;
import com.guild.calendar.dto.LoginEncryption;
import com.guild.calendar.dto.LoginRequest;
import com.guild.calendar.dto.LoginResponse;
import com.guild.calendar.dto.MemberForm;
import com.guild.calendar.dto.SuccessCode;
import com.guild.calendar.dto.UserDetail;

import com.guild.calendar.jwt.CustomTokenProvider;

import com.guild.calendar.jwt.token.IpUserDetailsToken;
import com.guild.calendar.redis.RedisService;
import com.guild.calendar.security.RSAEncryption;
import com.guild.calendar.service.MemberService;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;


@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

	private final MemberService memberService;
	private final CustomTokenProvider customTokenProvider;
	private final RedisService redisService;
	private final KeyPair keyPair;
	private final RSAEncryption rsaEncryption;



	@GetMapping("/publicKey")
	public ResponseEntity<?> publicKey(@RequestBody IsPublicKey publicKey){

		String IsPublicKey = publicKey.getPublicKey();
		byte[] publicKeyByte  = keyPair.getPublic().getEncoded();
		String stringPublicKey = Base64.getEncoder().encodeToString(publicKeyByte);


		if(stringPublicKey.equals(IsPublicKey)) {
			//return ResponseEntity.status(HttpStatus.OK).body(new SuccessCode(HttpStatus.OK.value(), 0L, "사용할 수 있는 publicKey 입니다."));
			return ResponseEntity.status(HttpStatus.OK).body("");
		}

		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
	}

	/**
	 * 로그인
	 * @param loginRequest
	 * @return ResponseEntity
	 */
	@PostMapping("/signin")
	public ResponseEntity<?> httpLogin(
			@RequestBody
			LoginRequest loginRequest) {

		//패스워드 복호화
		String plainPassword = decryptPassword(loginRequest.getSecret());
		loginRequest.setSecret(plainPassword);

		//사용자 조회
		UserDetail findMember = memberService.login(loginRequest);
		UserDetails userDetails = findMember.convertUserDetails();

		
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
		
		//중복 로그인 차단
		//서버에 저장한 JWT 토큰값 전달
		if(isNotHaveUsernameRedis(loginRequest.getEmail()))
		{
			String username = findMember.getUsername();
			IpUserDetailsToken ipUserDetailToken = (IpUserDetailsToken) customTokenProvider.findAccessToken(username,IpUserDetailsToken.class);

			LoginResponse token = new LoginResponse(ipUserDetailToken.getAccessToken(),ipUserDetailToken.getRefreshToken());
			token.setDocURL("https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/authentication");
			token.setTokenType("Bearer");

			return ResponseEntity.status(HttpStatus.OK).body(token);
		}

		//엑세스 토큰 생성
		IpUserDetailsToken ipUserDetailToken = (IpUserDetailsToken) customTokenProvider.generateToken(authentication, IpUserDetailsToken.class);
		ipUserDetailToken.setUserDetail(findMember);

		//토큰 Redis 저장
		redisService.setData(authentication.getName(), ipUserDetailToken);

		//redis 오류 대비 Email, AccessKey,RefreshKey,secretKey 로 DB 저장 구현하기


		LoginResponse token = new LoginResponse(ipUserDetailToken.getAccessToken(),ipUserDetailToken.getRefreshToken());
		token.setDocURL("https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/authentication");
		token.setTokenType("Bearer");
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}

	/**
	 * https 로그인 
	 * 패스워드는 평문 상태로 요청
	 * @param loginRequest
	 * @return ResponseEntity
	 */
	@PostMapping("/signin")
	public ResponseEntity<?> httpsLogin(
			@RequestBody
			LoginRequest loginRequest) {
		
		//사용자 조회
		UserDetail findMember = memberService.login(loginRequest);
		UserDetails userDetails = findMember.convertUserDetails();

		//인증토큰 생성
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

		//중복 로그인 차단
		//서버에 저장한 JWT 토큰값 전달
		if(isNotHaveUsernameRedis(loginRequest.getEmail()))
		{
			String username = findMember.getUsername();
			IpUserDetailsToken ipUserDetailToken = (IpUserDetailsToken) customTokenProvider.findAccessToken(username,IpUserDetailsToken.class);

			LoginResponse token = new LoginResponse(ipUserDetailToken.getAccessToken(),ipUserDetailToken.getRefreshToken());
			token.setDocURL("https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/authentication");
			token.setTokenType("Bearer");

			return ResponseEntity.status(HttpStatus.OK).body(token);
		}

		//엑세스 토큰 생성
		IpUserDetailsToken ipUserDetailToken = (IpUserDetailsToken) customTokenProvider.generateToken(authentication, IpUserDetailsToken.class);
		ipUserDetailToken.setUserDetail(findMember);

		//토큰 Redis 저장
		redisService.setData(authentication.getName(), ipUserDetailToken);

		//redis 오류 대비 Email, AccessKey,RefreshKey,secretKey 로 DB 저장 구현하기


		LoginResponse token = new LoginResponse(ipUserDetailToken.getAccessToken(),ipUserDetailToken.getRefreshToken());
		token.setDocURL("https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/authentication");
		token.setTokenType("Bearer");
		return ResponseEntity.status(HttpStatus.OK).body(token);
	}


	/**
	 * 로그아웃
	 * @param request
	 * @return
	 */
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {

		try {
			String accessToken = getJwtFromRequest(request);

			customTokenProvider.validateToken(accessToken);

			String username = customTokenProvider.getUserIdFromJWT(accessToken);

			//Redis에 유저 정보가 없으면 -102 에러가 발생합니다.
			haveUsernameRedis(username);
			//redisService.deleteData(username);

			return ResponseEntity.status(HttpStatus.OK).body("-1");
		} catch(ExpiredJwtException eje) {
			String username = eje.getClaims().getSubject();

			redisService.deleteData(username);

			String code = ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getCode();
			String message = ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getMessage();

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCode(HttpStatus.UNAUTHORIZED, code, message));
		} catch (UnsupportedJwtException se) {
			se.printStackTrace();

			String code = ExceptionCode.UNSUPPORTED_TOKEN.getCode();
			String message = ExceptionCode.UNSUPPORTED_TOKEN.getMessage();

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCode(HttpStatus.UNAUTHORIZED, code, message));
		} catch (SignatureException | MalformedJwtException mje) {
			mje.printStackTrace();

			String code = ExceptionCode.WRONG_TYPE_TOKEN.getCode();
			String message = ExceptionCode.WRONG_TYPE_TOKEN.getMessage();

			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCode(HttpStatus.UNAUTHORIZED, code, message));
		}

	}




	

	
	@GetMapping("/encrypt") 
	public ResponseEntity<?> loginencrypt(){
		
		LoginEncryption loginEncryption = new LoginEncryption();  
		
		loginEncryption.setAlgorithm(keyPair.getPublic().getAlgorithm());
		loginEncryption.setFormat(keyPair.getPublic().getFormat());
		loginEncryption.setEncoded(keyPair.getPublic().getEncoded());
		
		
		return ResponseEntity.status(HttpStatus.OK).body(loginEncryption);
	}
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody MemberForm memberForm) { 
		
		
		memberService.duplicateEmail(memberForm);
		
		//이메일 인증 추가하기 
		
		
		//회원가입 
		//memberService.saveMember(memberForm);
	
		return ResponseEntity.status(HttpStatus.OK).body(new SuccessCode(HttpStatus.OK.value(),"회원가입 완료"));
	}
	
	

	
	/**
	 * 
	 * @param encryptPassword
	 * @return
	 */
	private String decryptPassword(String encryptPassword) {
		String plainPassword = rsaEncryption.decrypt(encryptPassword, keyPair.getPrivate());
		
		if(plainPassword == "-106") {
			throw new IllegalArgumentException("-106");
		}
		
		return plainPassword;
	}
	
	
	/**
	 * 로그인한 유저인지 확인합니다.
	 * 
	 * @param username
	 */
	public boolean isNotHaveUsernameRedis(String username) {
		String redisUsername = redisService.findData(username);
		
		if(redisUsername != null) {
			return false;
		}
		return true;
	}
	

	
	@PostMapping("/refresh") 
	public ResponseEntity<?> createRefreshToken(HttpServletRequest request) {
		
		
		try {
			String requestAccessToken = getJwtFromRequest(request);
			String refreshToken = getRefreshJwtFromRequest(request);
			
			//토큰 유효성 검사
			customTokenProvider.validateToken(refreshToken);
			String username = customTokenProvider.getUserIdFromJWT(refreshToken);

			//redis에서 유저 데이터를 조회한다. Redis에 username이 없으면 로그아웃으로 처리된다.
			IpUserDetailsToken requestIpUserDetailsToken = (IpUserDetailsToken) customTokenProvider.findAccessToken(username,IpUserDetailsToken.class);
			
			//요청받은 AccessToken과 redis에 저장된 AccessToken을 비교합니다.
			String saveRedisAccessToken = requestIpUserDetailsToken.getAccessToken();
			isEqualsAccessToken(requestAccessToken,saveRedisAccessToken);
			
			
			//AccessToken 값이 일치하면 IpUserDetailsToken에서 유저 데이터로 Authentication을 만듭니다.
			UserDetails userDetails = requestIpUserDetailsToken.getUserDetail().convertUserDetails();
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
			
			
			//AccessToken과 RefreshToken을 생성합니다. 
			IpUserDetailsToken saveipUserDetailToken = (IpUserDetailsToken) customTokenProvider.generateToken(authentication, IpUserDetailsToken.class);
			saveipUserDetailToken.setUserDetail(requestIpUserDetailsToken.getUserDetail());
			
			//IP 접근도 사용할 경우 추가하기 
			//saveipUserDetailToken.setIpAdressList(requestIpUserDetailsToken.getIpAdressList());
			
			//Redis에서 기존 Token을 삭제하고 새로 생성한 Token을 저장한다.
			redisService.deleteData(username);
			redisService.setData(authentication.getName(), saveipUserDetailToken);
			
			
			LoginResponse token = new LoginResponse(saveipUserDetailToken.getAccessToken(),saveipUserDetailToken.getRefreshToken());
			
			return ResponseEntity.status(HttpStatus.OK).body(token);
		} catch(ExpiredJwtException eje) {
			//리프레시 토큰 유효기간 만료
			//리프레시 토큰이 만료되면 Redis에서 데이터를 삭제하고 로그아웃 처리한다.
			String username = eje.getClaims().getSubject();
			
			redisService.deleteData(username);
			
			String code = ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getCode();
			String message = ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getMessage();
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCode(HttpStatus.UNAUTHORIZED, code, message));
		} catch (UnsupportedJwtException se) {
			se.printStackTrace();
			String code = ExceptionCode.UNSUPPORTED_TOKEN.getCode();
			String message = ExceptionCode.UNSUPPORTED_TOKEN.getMessage();
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCode(HttpStatus.UNAUTHORIZED, code, message));	
		} catch (SignatureException | MalformedJwtException mje) {
			
			mje.printStackTrace();		
			
			String code = ExceptionCode.WRONG_TYPE_TOKEN.getCode();
			String message = ExceptionCode.WRONG_TYPE_TOKEN.getMessage();
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorCode(HttpStatus.UNAUTHORIZED, code, message));
		} 
	}
	
	
	
	
	/**
	 * 요청받은 AccessToken과 Redis에 저장된 AccessToken 값을 비교합니다. 일치하지 않으면 <b>IllegalArgumentException</b>을 발생시킵니다.
	 * 
	 * @param requestAccessToken
	 * @param saveRedisAccessToken
	 */
	private void isEqualsAccessToken(String requestAccessToken, String saveRedisAccessToken) {
		
		if(!requestAccessToken.equals(saveRedisAccessToken)) {
			throw new IllegalArgumentException("-104");
		}
		
	}

	public boolean haveUsernameRedis(String username) {
		boolean haveData= redisService.deleteData(username);
		if(!haveData) {
			throw new IllegalStateException("-102");
		}
		
		return haveData;
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
	/*
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
	*/
	
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
			ErrorCode errorCode = new  ErrorCode(HttpStatus.UNAUTHORIZED);
			//로그아웃 처리된 토큰 
			String code = ExceptionCode.LOGOUT_TOKEN.getCode();
			String message = ExceptionCode.LOGOUT_TOKEN.getMessage();
			
			errorCode.setErrorCodeDetail(code);
			errorCode.setMessage(message);
			
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
		
		
		
		if(errorMessage.equals("-104")) {
			String code = ExceptionCode.ILLEGAL_ACCESS_TOKEN.getCode();
			String message = ExceptionCode.ILLEGAL_ACCESS_TOKEN.getMessage();
			
			errorCode.setErrorCodeDetail(code);
			errorCode.setMessage(message);
			
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorCode);
		}
		
		if(errorMessage.equals("-106")) {
			String code = ExceptionCode.DECRYPTION_ERROR.getCode();
			String message = ExceptionCode.DECRYPTION_ERROR.getMessage();
			
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
