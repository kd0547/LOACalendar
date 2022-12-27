package com.guild.calendar.jwt;

import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.security.crypto.password.PasswordEncoder;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

public class EncryptUtil {
	private static final SignatureAlgorithm defaultSignatureAlgorithm = SignatureAlgorithm.HS256;
	
	private PasswordEncoder passwordEncoder;
	
	EncryptUtil(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
	
	
	
	public String createBaseKey(String username) {
		String secretKey = passwordEncoder.encode(username);
		
		return secretKey;
	}
	
	
	private SecretKey createSecret(String secretKey) {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}
	
}
