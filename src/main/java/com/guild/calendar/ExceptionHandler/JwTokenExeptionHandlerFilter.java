package com.guild.calendar.ExceptionHandler;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;


public class JwTokenExeptionHandlerFilter {
/* extends OncePerRequestFilter
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			
			filterChain.doFilter(request, response);
		} catch (ExpiredJwtException e) {
			// 토큰 유효기간 만료
		} catch (JwtException | IllegalArgumentException e) {
			// 유효하지 않은 토큰
		}
		
	}
	*/
	
}
