package com.guild.calendar.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.guild.calendar.entity.Guild;
import com.guild.calendar.entity.Member;

public interface GuildRepository extends JpaRepository<Guild, Long>{
	
	public List<Guild> findAllByGuildOwner(String guildOwner);
	
	public Guild findByIdAndGuildOwner(Member member,String guildOwner);
	
	public Guild findByMemberAndId(Member member,Long id);
	
	public Guild findByIdAndGuildOwner(Long id,String guildOwner);
	
	public Guild findByGuildOwnerAndGuildName(String guildOwner,String guildName);

	public Guild findByMemberAndGuildName(Member guildOwner,String guildName);
	
	
	
	public Guild findByGuildOwnerAndId(String username, Long id);
	
}
