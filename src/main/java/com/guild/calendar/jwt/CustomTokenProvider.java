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
	
	public JWToken generateToken(Authentication authentication,String useToken);
	
	public String getUserIdFromJWT(JWToken token);
	public String getUserIdFromJWT(String token,String secretKey);
	
	public JWToken createUseToken(String useToken,String accessKey,String refreshKey,String secretKey);
	public String createBaseKey(String username);
	
	public void validateToken(JWToken token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;
	public void validateToken(String token,String secretKey) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException;

	public JWToken findAccessToken(String accessToken, Class classType);
	
	public void deleteToken(String token);

	
}
