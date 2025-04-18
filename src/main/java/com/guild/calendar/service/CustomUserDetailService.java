package com.guild.calendar.service;

import com.guild.calendar.entity.Users;
import com.guild.calendar.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found with email: "+username));
        return User.builder()
                        .username(users.getEmail())
                        .password(users.getPassword())
                        .authorities(String.valueOf(users.getRole()))
                        .build();

    }

}
