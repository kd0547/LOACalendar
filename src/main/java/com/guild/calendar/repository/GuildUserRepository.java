package com.guild.calendar.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.guild.calendar.entity.GuildUser;

public interface GuildUserRepository extends JpaRepository<GuildUser, Long>{
	public GuildUser findByUsername(String username);
}
