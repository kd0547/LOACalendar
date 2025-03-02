package com.guild.calendar.service;

import com.guild.calendar.dto.SigninDto;
import com.guild.calendar.entity.Member;
import com.guild.calendar.exception.EmailDuplicateException;
import com.guild.calendar.repository.MemberRepository;
import com.guild.calendar.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long join(SigninDto signinDTO) {
        validateDuplicateMember(signinDTO);

        Member member = Member.signinMember(signinDTO);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(SigninDto member)  {
        Member findMember = memberRepository.findEmail(member.getEmail());
        if(!(findMember.getEmail().isEmpty())) {
            throw new EmailDuplicateException("사용 중인 이메일입니다.");
        }
    }

    @Transactional
    public Long login(SigninDto signinDTO) {

        Member member = memberRepository.findEmail(signinDTO.getEmail());
        if(member == null) {
            throw new UsernameNotFoundException("이메일이 존재하지 않습니다.");
        }

        if(!passwordEncoder.matches(signinDTO.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        return member.getId();
    }
}
