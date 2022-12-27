package com.guild.calendar.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.guild.calendar.dto.LoginRequest;
import com.guild.calendar.dto.MemberForm;
import com.guild.calendar.dto.UserDetail;
import com.guild.calendar.entity.Member;
import com.guild.calendar.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService{

	@Autowired
	private final MemberRepository memberRepository;
	
	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Member member = memberRepository.findByEmail(email);
		
		if(member == null) {
			throw new UsernameNotFoundException(email);
		}
		
		
		return User.builder()
					.username(member.getEmail())
					.password(member.getPassword())
					.roles(member.getRole().toString())
					.build();
	}
	
	public UserDetail findByEmail(LoginRequest loginRequest) {
		
		Member member = memberRepository.findByEmail(loginRequest.getEmail());
		
		if(member == null) {
			throw new UsernameNotFoundException(loginRequest.getEmail());
		}
		
		String rawPassword = loginRequest.getSecret();
		
		if(!passwordEncoder.matches(rawPassword,member.getPassword())) {
			throw new IllegalArgumentException("비밀번호를 확인하세요");
		}

		
		return UserDetail.builder()
				.username(member.getEmail())
				.password(member.getPassword())
				.role(member.getRole().toString())
				.build();
	}

	public void duplicateEmail(MemberForm memberForm) {
		String email = memberForm.getEmail();
		
		Member findMember = memberRepository.findByEmail(email);
		
		if(findMember != null) {
			throw new IllegalStateException("이미 가입된 회원입니다.");
		}	
	}

	/**
	 * 이메일 중복 가입 검사에서 예외가 없으면 DB에 저장
	 * 
	 * @param memberForm
	 * @return Long DB에 저장한 ID 값을 반환 
	 */
	public Long saveMember(MemberForm memberForm) {
		duplicateEmail(memberForm);
		Member member = Member.createMember(memberForm,passwordEncoder);
		
		Member saveMember = memberRepository.save(member);
		
		return saveMember.getId(); 
	}


	public MemberForm findMember(String email) {
		// TODO Auto-generated method stub
		return null;
	}

	


	

}
