package com.guild.calendar.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.guild.calendar.dto.ErrorCode;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{

	@Override
	public void commence(	HttpServletRequest request, 
							HttpServletResponse response,
							AuthenticationException authException) throws IOException, ServletException {
		String unauthorization = (String) request.getAttribute("unauthorization");
		
		System.out.println(unauthorization);
	}

}
