package com.guild.calendar.service;

import com.guild.calendar.entity.Users;
import com.guild.calendar.jwt.CustomUserDetails;
import com.guild.calendar.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users users = userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email: "+username));

        List<GrantedAuthority> authorities = List.of(
                new SimpleGrantedAuthority("ROLE_" + users.getRole())
        );
        CustomUserDetails customUserDetails = new CustomUserDetails(users.getId(), users.getUsername(),users.getPassword(),authorities);
        return customUserDetails;

    }

}
