package com.guild.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guild.calendar.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{
	
	public Member findByEmail(String email);
}
