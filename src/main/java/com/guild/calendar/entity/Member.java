package com.guild.calendar.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.guild.calendar.constant.Role;
import com.guild.calendar.dto.MemberForm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Member extends BaseEntity{
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "member_SEQ_GENERATOR")
	@Column(name = "member_id")
	private Long id;
	
	@Column(unique = true)
	private String email;
	
	@Column(nullable = false)	//사용자이름 - 닉네임 
	private String username;
	
	
	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	private Role role;
	
	public static Member createMember(MemberForm memberForm,PasswordEncoder passwordEncoder) {
		Member member = new Member();
		String encodePassword = passwordEncoder.encode(memberForm.getPassword());
		
		member.setEmail(memberForm.getEmail());
		member.setPassword(encodePassword);
		member.setUsername(memberForm.getUsername());
		member.setRole(Role.USER);
		
		return member;
	}
	

	
}

