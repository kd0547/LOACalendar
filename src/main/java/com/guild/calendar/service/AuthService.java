package com.guild.calendar.service;

import com.guild.calendar.dto.MemberDto;
import com.guild.calendar.dto.SigninDto;
import com.guild.calendar.entity.Users;
import com.guild.calendar.exception.EmailDuplicateException;
import com.guild.calendar.repository.MemberRepository;
import com.guild.calendar.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(SigninDto signinDTO) {
        validateDuplicateMember(signinDTO);
        signinDTO.setPassword(passwordEncoder.encode(signinDTO.getPassword()));

        Users user = Users.signinMember(signinDTO);
        Users saved = userRepository.save(user);
        return saved.getId();
    }

    private void validateDuplicateMember(SigninDto member)  {
        if (userRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new EmailDuplicateException("사용 중인 이메일입니다.");
        }
    }

}
