package com.guild.calendar.jwt;



import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.guild.calendar.jwt.token.IpUserDetailsToken;
import com.guild.calendar.jwt.token.JWToken;
import com.guild.calendar.jwt.token.UserDetailsToken;
import com.guild.calendar.redis.RedisService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;



//https://covenant.tistory.com/241
//https://bcp0109.tistory.com/328
//https://velog.io/@shinmj1207/Spring-Spring-Security-JWT-%EB%A1%9C%EA%B7%B8%EC%9D%B8
//https://velog.io/@_woogie/JWT-%EB%A1%9C%EA%B7%B8%EC%9D%B8%EB%B0%A9%EC%8B%9D-%EA%B5%AC%ED%98%84%ED%95%98%EA%B8%B0-feat.-session%EC%97%90%EC%84%9C-jwt%EB%A1%9C
//https://gksdudrb922.tistory.com/217
//https://auth0.com/docs/quickstart/webapp
//https://developer.okta.com/docs/guides/build-self-signed-jwt/java/main/
//https://velog.io/@dhk22/TIL-Day-29
//https://sy34.net/public-private-key-pair/
//https://aonee.tistory.com/72


public class JwtTokenProvider implements CustomTokenProvider{
	
	
	private static final ChronoUnit AccessTokenValidMinutes = ChronoUnit.MINUTES;
	private static final ChronoUnit RefreshTokenValidMinutes = ChronoUnit.HOURS;
	
	private static final SignatureAlgorithm defaultSignatureAlgorithm = SignatureAlgorithm.HS256;
	
	private static final Long AccessTokenTime = 30L;
	private static final Long RefreshTokenTime = 24L;
	
	
	private final PasswordEncoder passwordEncoder;
	
	
	private final RedisService redisService;
	
	public JwtTokenProvider (PasswordEncoder passwordEncoder,RedisService redisService) {
		this.passwordEncoder = passwordEncoder;
		this.redisService = redisService;
	}
	
	
	@Override
    public JWToken generateToken(Authentication authentication,String useToken) {
    	
    	if(useToken == null || authentication == null) {
    		throw new NullPointerException();
    	}
   
    	String username = authentication.getName();
    	
    	Claims claims = Jwts.claims().setSubject(username);
    	claims.put("roles", authentication.getAuthorities());
    	String secretKey = createBaseKey(username);
    	SecretKey key = createSecret(secretKey);
    	
    	Instant now = Instant.now();
    	
    	String accessToken = Jwts.builder()
    			.setClaims(claims)
    			.setIssuedAt(new Date().from(now))
    			.setExpiration(new Date().from(now.plus(AccessTokenTime, AccessTokenValidMinutes))) //
    			.setId(UUID.randomUUID().toString())
    			.signWith(key, defaultSignatureAlgorithm)
    			.compact();
    	
    	String refreshToken = Jwts.builder()
    			.setClaims(claims)
    			.setIssuedAt(new Date().from(now))
    			.setExpiration(new Date().from(now.plus(RefreshTokenTime, RefreshTokenValidMinutes))) //
    			.setId(UUID.randomUUID().toString())
    			.signWith(key, defaultSignatureAlgorithm)
    			.compact();
    	    	
    	return createUseToken(useToken,accessToken,refreshToken,secretKey);
    }
	
	
	@Override
	public String getUserIdFromJWT(JWToken token) {
		
		
		return getUserIdFromJWT(token.getAccessToken(),token.getKey());
	}

	
	@Override
    public String getUserIdFromJWT(String token,String secretKey) {
		SecretKey key = createSecret(secretKey);
		
    	Claims claims = Jwts.parserBuilder()
    			.setSigningKey(key)
    			.build()
    			.parseClaimsJws(token)
    			.getBody();
    						

        return claims.getSubject();
    }
	@Override
	public void validateToken(JWToken token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException{
		
		this.validateToken(token.getAccessToken(),token.getKey());
	}
	
    
    @Override
    public void validateToken(String token,String secretKey) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException{
    	SecretKey key = createSecret(secretKey);
    	
    	Jwts.parserBuilder()
		.setSigningKey(key)
		.build()
		.parseClaimsJws(token);
    }
    
    @Override
    public JWToken createUseToken(String useToken,String accessKey,String refreshKey,String secretKey) {
    	
    	
    	if(useToken.equals("ipUserDetailsToken")) {
    		return new IpUserDetailsToken(null, accessKey, refreshKey, secretKey);
    	} else if(useToken.equals("userDetailsToken")) {
    		return new UserDetailsToken(accessKey, refreshKey, secretKey);
    	}
    	
		return null;
    }
    
	private SecretKey createSecret(String secretKey) {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}


	@Override
	public String createBaseKey(String username) {
		String secretKey = passwordEncoder.encode(username);
		
		
		return secretKey;
	}

	@Override
	public JWToken findAccessToken(String accessToken,Class classType) {
		
		
		return (JWToken) redisService.getData(accessToken, classType);
	}


	@Override
	public void deleteToken(String token) {
		redisService.deleteData(token);
		
	}
	


}
