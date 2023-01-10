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
	 * @param authentication
	 * @param useToken
	 * @return
	 */
	@Deprecated
	public JWToken generateToken(Authentication authentication,String useToken);
	
	/**
	 * 
	 * @param authentication
	 * @return
	 */
	public JWToken generateToken(Authentication authentication);
	
	public JWToken generateToken(String username);
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	public String getUserIdFromJWT(String token);
	
	/**
	 * 
	 * @param token
	 * @return
	 */
	@Deprecated
	public String getUserIdFromJWT(JWToken token);
	
	/**
	 * 
	 * @param token
	 * @param secretKey
	 * @return
	 */
	@Deprecated
	public String getUserIdFromJWT(String token,String secretKey);
	
	/**
	 * 
	 * @param useToken
	 * @param accessKey
	 * @param refreshKey
	 * @return
	 */
	public JWToken createUseToken(String useToken, String accessKey, String refreshKey);
	
	/**
	 * 
	 * @param useToken
	 * @param accessKey
	 * @param refreshKey
	 * @param secretKey
	 * @return
	 */
	@Deprecated
	public JWToken createUseToken(String useToken,String accessKey,String refreshKey,String secretKey);
	
	
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
	 * @param token
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 */
	@Deprecated
	public void validateToken(JWToken token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
	
	/**
	 * 
	 * @param token
	 * @param secretKey
	 * @throws ExpiredJwtException
	 * @throws UnsupportedJwtException
	 * @throws MalformedJwtException
	 * @throws SignatureException
	 * @throws IllegalArgumentException
	 */
	@Deprecated
	public void validateToken(String token,String secretKey) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
	
	
	/**
	 * 
	 * @param accessToken
	 * @param classType
	 * @return
	 */
	public JWToken findAccessToken(String accessToken, Class classType);
	
	/**
	 * 
	 * @param token
	 */
	public void deleteToken(String token);

	
	/**
	 * 
	 * @param username
	 * @return
	 */
	public String createBaseKey(String username);

	public JWToken generateToken(Authentication authentication, Class t);

	public JWToken createUseToken(Class useToken, String accessKey, String refreshKey);

	

	

	
}
