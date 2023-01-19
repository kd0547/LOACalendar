package com.guild.calendar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.guild.calendar.entity.Guild;
import com.guild.calendar.entity.GuildUser;
import com.guild.calendar.entity.CalendarDetail;

public interface GuildUserRepository extends JpaRepository<GuildUser, Long>{
	public GuildUser findByUsername(String username);
	
	public List<GuildUser> findByGuild(Guild guild);
	
	public Long countByGuild(Guild guild);
	
	public GuildUser findByGuildNameAndUsername(String guild,String username);
	
	public GuildUser findByGuildAndId(Guild guildId,Long id);

	public Optional<GuildUser> findById(Long GuildId);
	
	@Query("select gu from GuildUser gu where gu.guildName = ?1 and gu.guild.id = ?2")
	public List<GuildUser> findByGuildNameAndGuild(String guildName, Long guildId);
	
}
