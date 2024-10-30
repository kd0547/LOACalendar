package com.guild.calendar.service;

import com.guild.calendar.security.RSAEncryption;
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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService{

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	private final RSAEncryption rsaEncryption;


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


	/**
	 *
	 * @param loginRequest
	 * @return
	 */
	public UserDetail login(LoginRequest loginRequest) {

		Member member = memberRepository.findByEmail(loginRequest.getEmail());

		if(member == null) {
			throw new UsernameNotFoundException("-101");
		}

		String rawPassword = loginRequest.getSecret();

		if(!passwordEncoder.matches(rawPassword,member.getPassword())) {
			throw new IllegalArgumentException("-101");
		}


		return UserDetail.builder()
				.username(member.getEmail())
				.password(member.getPassword())
				.role(member.getRole().toString())
				.build();
	}

	/**
	 *
	 * @param loginRequest
	 * @return
	 */
	public UserDetail findByEmail(LoginRequest loginRequest) {
		
		Member member = memberRepository.findByEmail(loginRequest.getEmail());
		
		if(member == null) {
			throw new UsernameNotFoundException("-101");
		}
		
		String rawPassword = loginRequest.getSecret();
		
		if(!passwordEncoder.matches(rawPassword,member.getPassword())) {
			throw new IllegalArgumentException("-101");
		}

		
		return UserDetail.builder()
				.username(member.getEmail())
				.password(member.getPassword())
				.role(member.getRole().toString())
				.build();
	}
	/**
	 * 
	 * 이메일을 중복 검사 중복된 이메일이 있는 경우 <b>IllegalStateException</b> 이 발생한다.</br>
	 * 
	 * @param memberForm
	 * @exception IllegalStateException
	 * 
	 */
	public void duplicateEmail(MemberForm memberForm) {
		String email = memberForm.getEmail();
		
		Member findMember = memberRepository.findByEmail(email);
		
		if(findMember != null) {
			throw new IllegalStateException("-100");
		}	
	}

	/**
	 * 유저 저장
	 * @param memberForm
	 * @return Long DB에 저장한 ID 값을 반환 
	 */
	@Transactional
	public Long saveMember(MemberForm memberForm) {
		//이메일 중복 검사
		duplicateEmail(memberForm);

		Member member = Member.createMember(memberForm,passwordEncoder);
		Member saveMember = memberRepository.save(member);
		
		return saveMember.getId(); 
	}

	public Long findByEmail(String username) {
		Member member = memberRepository.findByEmail(username);
		return member.getId();
	}
}
