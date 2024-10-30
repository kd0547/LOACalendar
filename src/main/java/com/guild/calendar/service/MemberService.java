package com.guild.calendar.service;

import com.guild.calendar.dto.SigninDTO;
import com.guild.calendar.entity.Member;
import com.guild.calendar.exception.EmailDuplicateException;
import com.guild.calendar.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    @Transactional
    public Long join(SigninDTO signinDTO) {
        validateDuplicateMember(signinDTO);

        Member member = Member.signinMember(signinDTO);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(SigninDTO member)  {
        Member findMember = memberRepository.findEmail(member.getEmail());
        if(!(findMember.getEmail().isEmpty())) {
            throw new EmailDuplicateException("사용 중인 이메일입니다.");
        }
    }
}
