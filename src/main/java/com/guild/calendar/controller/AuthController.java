package com.guild.calendar.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.util.Base64;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.guild.calendar.Exception.ExceptionCode;
import com.guild.calendar.dto.ErrorCode;
import com.guild.calendar.dto.IsPublicKey;
import com.guild.calendar.dto.LoginEncryption;
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
import com.guild.calendar.security.RSAEncryption;
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
	
	@Autowired
	private KeyPair keyPair;
	
	@Autowired
	private RSAEncryption rsaEncryption;
	
	@GetMapping("/isPublicKey")
	public ResponseEntity<?> publicKey(@RequestBody IsPublicKey publicKey){
		
		String IsPublicKey = publicKey.getPublicKey();
		byte[] publicKeyByte  = keyPair.getPublic().getEncoded();
		String stringPublicKey = Base64.getEncoder().encodeToString(publicKeyByte);
		
		
		if(stringPublicKey.equals(IsPublicKey)) {
			//return ResponseEntity.status(HttpStatus.OK).body(new SuccessCode(HttpStatus.OK.value(), 0L, "????????? ??? ?????? publicKey ?????????."));
			return ResponseEntity.status(HttpStatus.OK).body("");
		}
	
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
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
		
		//????????? ?????? ???????????? 
		
		
		//???????????? 
		//memberService.saveMember(memberForm);
	
		return ResponseEntity.status(HttpStatus.OK).body(new SuccessCode(HttpStatus.OK.value(),"???????????? ??????"));
	}
	
	
	
	/**
	 * 
	 * ????????? API ?????????. RequestBody?????? ???????????? ?????? ??????????????? ????????? ???, ????????? ??????????????????. ????????? ???????????? ????????? ????????? ???????????? ????????? ???????????????
	 * ?????? ?????? ??????????????? ???????????? ?????? ??????, <b>UsernameNotFoundException</b>,<b>IllegalArgumentException</b>??? ???????????????. ??????????????? -101 ?????????.
	 * ?????? ???????????? ????????? API??? ????????? ?????? IllegalStateException??? ???????????????. ??????????????? -105 ?????????.
	 * @param loginRequest
	 * @return ResponseEntity
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
		
		
		
		String plainPassword = decryptPassword(loginRequest.getSecret());
		
		loginRequest.setSecret(plainPassword);
		
		
		
		UserDetail findMember = memberService.findByEmail(loginRequest);
		UserDetails userDetails = findMember.convertUserDetails();
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
		
		
		//????????? ??????
		if( isNotHaveUsernameRedis(loginRequest.getEmail())) {
			String username = findMember.getUsername();
			IpUserDetailsToken ipUserDetailToken = (IpUserDetailsToken) customTokenProvider.findAccessToken(username,IpUserDetailsToken.class);
			
			LoginResponse token = new LoginResponse(ipUserDetailToken.getAccessToken(),ipUserDetailToken.getRefreshToken());
			token.setDocURL("https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/authentication");
			token.setTokenType("Bearer");
			
			return ResponseEntity.status(HttpStatus.OK).body(token);
		} else {
			//????????? ?????? ??????
			IpUserDetailsToken ipUserDetailToken = (IpUserDetailsToken) customTokenProvider.generateToken(authentication, IpUserDetailsToken.class);
			ipUserDetailToken.setUserDetail(findMember);
			
			//?????? Redis ??????
			redisService.setData(authentication.getName(), ipUserDetailToken);
			
			//redis ?????? ?????? Email, AccessKey,RefreshKey,secretKey ??? DB ?????? ????????????
			
			
			LoginResponse token = new LoginResponse(ipUserDetailToken.getAccessToken(),ipUserDetailToken.getRefreshToken());
			token.setDocURL("https://app.gitbook.com/o/2Kxp9w9wD6czxO5f7Vpa/s/4c6Lnb6whYxpAx2A81Na/reference/v1.0/authentication");
			token.setTokenType("Bearer");
			return ResponseEntity.status(HttpStatus.OK).body(token);
		}
		
		
		
		
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
	 * ???????????? ???????????? ???????????????.
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
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request) {
		
		try {
			String accessToken = getJwtFromRequest(request);
			
			customTokenProvider.validateToken(accessToken);
			
			String username = customTokenProvider.getUserIdFromJWT(accessToken);
			
			//Redis??? ?????? ????????? ????????? -102 ????????? ???????????????.
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
	
	@PostMapping("/refresh") 
	public ResponseEntity<?> createRefreshToken(HttpServletRequest request) {
		
		
		try {
			String requestAccessToken = getJwtFromRequest(request);
			String refreshToken = getRefreshJwtFromRequest(request);
			
			//?????? ????????? ??????
			customTokenProvider.validateToken(refreshToken);
			String username = customTokenProvider.getUserIdFromJWT(refreshToken);

			//redis?????? ?????? ???????????? ????????????. Redis??? username??? ????????? ?????????????????? ????????????.
			IpUserDetailsToken requestIpUserDetailsToken = (IpUserDetailsToken) customTokenProvider.findAccessToken(username,IpUserDetailsToken.class);
			
			//???????????? AccessToken??? redis??? ????????? AccessToken??? ???????????????.
			String saveRedisAccessToken = requestIpUserDetailsToken.getAccessToken();
			isEqualsAccessToken(requestAccessToken,saveRedisAccessToken);
			
			
			//AccessToken ?????? ???????????? IpUserDetailsToken?????? ?????? ???????????? Authentication??? ????????????.
			UserDetails userDetails = requestIpUserDetailsToken.getUserDetail().convertUserDetails();
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
			
			
			//AccessToken??? RefreshToken??? ???????????????. 
			IpUserDetailsToken saveipUserDetailToken = (IpUserDetailsToken) customTokenProvider.generateToken(authentication, IpUserDetailsToken.class);
			saveipUserDetailToken.setUserDetail(requestIpUserDetailsToken.getUserDetail());
			
			//IP ????????? ????????? ?????? ???????????? 
			//saveipUserDetailToken.setIpAdressList(requestIpUserDetailsToken.getIpAdressList());
			
			//Redis?????? ?????? Token??? ???????????? ?????? ????????? Token??? ????????????.
			redisService.deleteData(username);
			redisService.setData(authentication.getName(), saveipUserDetailToken);
			
			
			LoginResponse token = new LoginResponse(saveipUserDetailToken.getAccessToken(),saveipUserDetailToken.getRefreshToken());
			
			return ResponseEntity.status(HttpStatus.OK).body(token);
		} catch(ExpiredJwtException eje) {
			//???????????? ?????? ???????????? ??????
			//???????????? ????????? ???????????? Redis?????? ???????????? ???????????? ???????????? ????????????.
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
	 * ???????????? AccessToken??? Redis??? ????????? AccessToken ?????? ???????????????. ???????????? ????????? <b>IllegalArgumentException</b>??? ??????????????????.
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
			//???????????? ????????? ?????? 
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
