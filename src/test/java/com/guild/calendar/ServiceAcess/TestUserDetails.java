package com.guild.calendar.ServiceAcess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.guild.calendar.constant.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TestUserDetails implements UserDetailsService{
	
	@Autowired
	private final PasswordEncoder passwordEncoder;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		String userName = "test@naver.com";
		String password = passwordEncoder.encode("1234");
		Role role = Role.USER;
		
		return User.builder()
				.username(userName)
				.password(password)
				.roles(role.toString())
				.build()
				;
	}
	
	
	
}
