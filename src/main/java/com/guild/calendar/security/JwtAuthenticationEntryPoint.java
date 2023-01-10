package com.guild.calendar.security;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.guild.calendar.Exception.ExceptionCode;
import com.guild.calendar.dto.ErrorCode;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint{
	
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void commence(	HttpServletRequest request, 
							HttpServletResponse response,
							AuthenticationException authException) throws IOException, ServletException {
		String unauthorization = (String) request.getAttribute("unauthorization");
		
		if(unauthorization != null) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType(MediaType.APPLICATION_JSON.toString());
			response.setCharacterEncoding("utf-8");
			PrintWriter print = response.getWriter();
			ErrorCode errorCode = new ErrorCode(HttpStatus.UNAUTHORIZED);
			
			if(unauthorization.equals("-000")) {
				
				errorCode.setErrorCodeDetail(ExceptionCode.NO_TOKEN.getCode());
				errorCode.setMessage(ExceptionCode.NO_TOKEN.getMessage());
			}
			
			if(unauthorization.equals("-001")) {
				errorCode.setErrorCodeDetail(ExceptionCode.WRONG_TYPE_TOKEN.getCode());
				errorCode.setMessage(ExceptionCode.WRONG_TYPE_TOKEN.getMessage());
			}
			
			if(unauthorization.equals("-002")) {
				errorCode.setErrorCodeDetail(ExceptionCode.UNSUPPORTED_TOKEN.getCode());
				errorCode.setMessage(ExceptionCode.UNSUPPORTED_TOKEN.getMessage());
			}
			
			if(unauthorization.equals("-102")) {
				errorCode.setErrorCodeDetail(ExceptionCode.LOGOUT_TOKEN.getCode());
				errorCode.setMessage(ExceptionCode.LOGOUT_TOKEN.getMessage());
			}
			
			if(unauthorization.equals("-103")) {
				errorCode.setErrorCodeDetail(ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getCode());
				errorCode.setMessage(ExceptionCode.LOGOUT_TOKEN_TIMEOUT.getMessage());
			}
			
			String errorCodeString = objectMapper.writeValueAsString(errorCode);
			print.write(errorCodeString);
			
		}
		
		
	}

}
