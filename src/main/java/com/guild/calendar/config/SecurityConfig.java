package com.guild.calendar.config;

import java.util.Arrays;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.guild.calendar.jwt.CustomTokenProvider;
import com.guild.calendar.jwt.JwtAuthenticationFilter;
import com.guild.calendar.jwt.JwtTokenProvider;
import com.guild.calendar.redis.RedisService;
import com.guild.calendar.security.JwtAuthenticationEntryPoint;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
	
	@Autowired
	private final JwtAuthenticationEntryPoint unAuthorizedHandler;
	
	@Autowired
	private final RedisService redisService;
	
	@Value("${jwt.token.key}")
	private String key;
	
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http
			.cors()
		.and()
			.csrf().disable()
			.exceptionHandling()
			.authenticationEntryPoint(unAuthorizedHandler)
		.and()
			.sessionManagement()
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
            .authorizeRequests() 
            .antMatchers("/calendar/share/url/**","/auth/**").permitAll()
            .antMatchers("/**").authenticated()
        .and()
            .exceptionHandling().authenticationEntryPoint(unAuthorizedHandler).and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .formLogin().disable().headers().frameOptions().disable();
		;


		/**
		 * JWT 토큰
		 */
		http.addFilterBefore(
				new JwtAuthenticationFilter(
						jwTokenProvider()),
				UsernamePasswordAuthenticationFilter.class);
		
		
		
		return http.build();
	}
	
	@Bean
	public CustomTokenProvider jwTokenProvider() {
		CustomTokenProvider customTokenProvider =  new JwtTokenProvider(passwordEncoder(),redisService,key);
		
		return customTokenProvider;
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
	
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
