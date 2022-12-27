package com.guild.calendar.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.guild.calendar.entity.RaidPlanGuildUser;

public interface RaidPlanGuildUserRepository extends JpaRepository<RaidPlanGuildUser, Long>{
	
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param guildName
	 * @return
	 */
	@Query(value = "select * from raid_plan_guild_user u where (u.guild_name = ?1) and ( u.raid_start_date_time between ?2 AND  ?3);"
			,nativeQuery = true)
	public List<RaidPlanGuildUser> selectByRaidStartDateWhereGuildName(String guildName,LocalDateTime startDate,LocalDateTime endDate);

	
}
