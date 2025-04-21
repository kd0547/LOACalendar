package com.guild.calendar.entity;

import com.guild.calendar.dto.SigninDto;
import jakarta.persistence.*;

import com.guild.calendar.constant.Role;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @ToString
public class Users {
	
	@Id
	@GeneratedValue (strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(unique = true)
	private String email;
	
	@Column(nullable = false)	//사용자이름 - 닉네임 
	private String username;

	@Column(nullable = false)
	private String password;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Role role = Role.USER;

	@OneToMany(mappedBy = "users",fetch = FetchType.LAZY)
	private List<Calendar> calendars = new ArrayList<>();

	@OneToMany(mappedBy = "users",fetch = FetchType.LAZY)
	private List<Guild> guilds = new ArrayList<>();

	@Column(nullable = false)
	private Boolean isActive = true;	//계정 활성화

	private int CountCalendar;

	@Column(nullable = false)
	private Boolean isEmailVerified = false; //이메일 인증 상태


	@OneToMany(mappedBy = "users",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
	private List<DiscodeInfo> discodeInfos;

	public Users() {}
	public Users(Long userId) {
		this.id = userId;
	}


	public static Users signinMember(SigninDto signinDTO) {
		Users users = new Users();

		users.setEmail(signinDTO.getEmail());
		users.setUsername(signinDTO.getUsername());
		users.setPassword(signinDTO.getPassword());

		return users;
	}

}

