package com.guild.calendar.jwt;

import java.security.Key;


import javax.crypto.SecretKey;

import org.springframework.security.core.Authentication;


import com.guild.calendar.jwt.token.JWToken;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

public interface CustomTokenProvider {
	
	
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	public String getUserIdFromJWT(String token);

	
	/**
	 * 
	 * @param token
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 */
	public void validateToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
	

	
	/**
	 * 
	 * @param accessToken
	 * @param classType
	 * @return
	 */
	public JWToken findAccessToken(String accessToken, Class classType);
	

	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public String createBaseKey(String username);

	/**
	 * 토큰 발급
	 * @param authentication
	 * @param t
	 * @return
	 */
	public JWToken generateToken(Authentication authentication, Class t);

	public JWToken createUseToken(Class useToken, String accessKey, String refreshKey);

	

	

	
}
