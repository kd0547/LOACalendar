package com.guild.calendar.entity;

import com.guild.calendar.dto.SigninDTO;
import jakarta.persistence.*;

import com.guild.calendar.entity.base.BaseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.guild.calendar.constant.Role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class Member extends BaseEntity {
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO, generator = "member_SEQ_GENERATOR")
	@Column(name = "member_id")
	private Long id;
	
	@Column(unique = true)
	private String email;
	
	@Column(nullable = false)	//사용자이름 - 닉네임 
	private String username;

	@Column(nullable = false)
	private String password;	//
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role = Role.USER;

	@Column(nullable = false)
	private Boolean isActive = true;	//계정 활성화

	private int CountCalendar;

	@Column(nullable = false)
	private Boolean isEmailVerified = false; //이메일 인증 상태



	public static Member signinMember(SigninDTO signinDTO) {
		Member member = new Member();

		member.setEmail(signinDTO.getEmail());
		member.setUsername(signinDTO.getUsername());
		member.setPassword(signinDTO.getPassword());

		return member;
	}

}

