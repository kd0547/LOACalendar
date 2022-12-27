package com.guild.calendar.jwt;

import java.io.IOException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.guild.calendar.dto.UserDetail;
import com.guild.calendar.jwt.token.IpUserDetailsToken;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;


public class JwtAuthenticationFilter extends OncePerRequestFilter{
	
	
	private final CustomTokenProvider customTokenProvider;
	
	
	
	
	public JwtAuthenticationFilter(CustomTokenProvider customTokenProvider) {
		this.customTokenProvider = customTokenProvider;
		
		
	}
	

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		
		
		try {
			String accessToken = getJwtFromRequest(request);
			if(accessToken == null) {
				throw new NullPointerException();
			}
			
			IpUserDetailsToken findIpUserDetailsToken = (IpUserDetailsToken) customTokenProvider.findAccessToken(accessToken,IpUserDetailsToken.class);
			
			customTokenProvider.validateToken(findIpUserDetailsToken);
			
			UserDetails userDetails =  findIpUserDetailsToken.getUserDetail().convertUserDetails();
			Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (NullPointerException ne) {
			//ne.printStackTrace();
			request.setAttribute("unauthorization",ExceptionCode.NO_TOKEN.getCode());
			
		} catch (ExpiredJwtException eje) {
			//eje.printStackTrace();
			//request.setAttribute("unauthorization", "test1234");
			
			
		} catch (UnsupportedJwtException se) {
			//se.printStackTrace();
			request.setAttribute("unauthorization",ExceptionCode.UNSUPPORTED_TOKEN.getCode());
			
		} catch (SignatureException | IllegalArgumentException | MalformedJwtException mje) {
			
			//mje.printStackTrace();
			
		}  catch (Exception e) {
			//e.printStackTrace();
			
		}
		
		
		
		
		filterChain.doFilter(request, response);
	}
	
	private String getJwtFromRequest(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");
		
		if(bearerToken != null && bearerToken.startsWith("Bearer ")) {
			return bearerToken.substring("Bearer ".length());
		}
		
		
		return null;
	}

}
