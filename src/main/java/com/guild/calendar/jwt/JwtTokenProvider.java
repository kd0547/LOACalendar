package com.guild.calendar.jwt;



import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.UUID;
import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import lombok.RequiredArgsConstructor;



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

	private static final ChronoUnit AccessTokenValidMinutes = ChronoUnit.DAYS; //Access 토큰 만료
	private static final ChronoUnit RefreshTokenValidMinutes = ChronoUnit.DAYS;
	
	private static final SignatureAlgorithm defaultSignatureAlgorithm = SignatureAlgorithm.HS256;
	
	
	private String secretKey;
	
	@Deprecated
	private static Key key;
	
	private static final Long AccessTokenTime = 30L;
	private static final Long RefreshTokenTime = 30L;
	
	
	private final PasswordEncoder passwordEncoder;
	private final RedisService redisService;
	
	public JwtTokenProvider (PasswordEncoder passwordEncoder,RedisService redisService,String key) {
		this.passwordEncoder = passwordEncoder;
		this.redisService = redisService;
		this.secretKey = key;
	}

	/**
	 * 토큰 발급
	 * @param authentication
	 * @param TokenType
	 * @return
	 */
	@Override
    public JWToken generateToken(Authentication authentication,Class TokenType) {
    	if(authentication == null) {
    		throw new NullPointerException();
    	}
		//id 가져오기
    	String username = authentication.getName();
    	
    	Claims claims = Jwts.claims()
    			.setSubject(username);
    	claims.put("roles", authentication.getAuthorities());

    	SecretKey key = createSecret(secretKey);
    	
    	Instant now = Instant.now();

		String accessToken = Jwts.builder()
    			.setClaims(claims)
    			.setIssuedAt(Date.from(now))
    			.setExpiration(Date.from(now.plus(AccessTokenTime, AccessTokenValidMinutes))) //
    			.setId(UUID.randomUUID().toString())
    			.signWith(key, defaultSignatureAlgorithm)
    			.compact();
		String refreshToken = Jwts.builder()
    			.setClaims(claims)
    			.setIssuedAt(Date.from(now))
    			.setExpiration(Date.from(now.plus(RefreshTokenTime, RefreshTokenValidMinutes))) //
    			.setId(UUID.randomUUID().toString())
    			.signWith(key, defaultSignatureAlgorithm)
    			.compact();
    	    	
    	return createUseToken(TokenType,accessToken,refreshToken);
    }

	/**
	 * 토큰 생성
	 * @param claims
	 * @param createTime
	 * @param TokenTime
	 * @param chronoUnit
	 * @return
	 */
	private String CreateJWTToken(Claims claims,Instant createTime,Long TokenTime,ChronoUnit chronoUnit) {


		return  Jwts.builder()
				.setClaims(claims)
				.setIssuedAt(Date.from(createTime)) //토큰 생성 날짜
				.setExpiration(Date.from(createTime.plus(TokenTime, chronoUnit))) //토큰 만료 날짜
				.setId(UUID.randomUUID().toString())
				.signWith(key, defaultSignatureAlgorithm)
				.compact();
	}


	


	@Override
    public String getUserIdFromJWT(String token) {
		SecretKey key = createSecret(secretKey);
		
    	Claims claims = Jwts.parserBuilder()
    			.setSigningKey(key)
    			.build()
    			.parseClaimsJws(token)
    			.getBody();
    						

        return claims.getSubject();
    }

	@Override
	public void validateToken(String token) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException,
			SignatureException, IllegalArgumentException {
		SecretKey key = createSecret(secretKey);
		
		
		
		Jwts.parserBuilder()
		.setSigningKey(key)
		.build()
		.parseClaimsJws(token);
	}
    

    
    
    
    
    @Override
    public JWToken createUseToken(Class useToken,String accessKey,String refreshKey) {
    	
    	
    	if(isIpUserDetailsToken(useToken)) {
    		
    		return new IpUserDetailsToken(null, accessKey, refreshKey,null);
    	} else if(isUserDetailsToken(useToken)) {
    		
    		return new UserDetailsToken(accessKey, refreshKey, null);
    	}
    	
		return null;
    }
    
    private boolean isUserDetailsToken(Class classType) {
		
		String type = classType.getName();
		
		return UserDetailsToken.class.getName().equals(type);
	}
    
    private boolean isIpUserDetailsToken(Class classType) {
		String type = classType.getName();
		
		return IpUserDetailsToken.class.getName().equals(type);
	}
    

	private SecretKey createSecret(String secretKey) {
		return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}


	@Override
	public JWToken findAccessToken(String accessToken,Class classType) {
		
	
		return (JWToken) redisService.getData(accessToken, classType);
	}
}
