package com.guild.calendar.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.guild.calendar.entity.Calendar;
import com.guild.calendar.entity.CalendarDetail;
import com.guild.calendar.entity.RaidPlan;

public interface CalendarDetailRepository extends JpaRepository<CalendarDetail, Long>{
	
	
	/**
	 * 
	 * @param startDate
	 * @param endDate
	 * @param guildName
	 * @return
	 */
	@Query(value = "select * from raid_plan_guild_user u where (u.guild_name = ?1) and ( u.raid_start_date_time between ?2 AND  ?3);"
			,nativeQuery = true)
	public List<CalendarDetail> selectByRaidStartDateWhereGuildName(String guildName,LocalDateTime startDate,LocalDateTime endDate);
	
	@Query("select c from CalendarDetail c where (c.calendar.id = ?1) and (c.startDate between ?2 and ?3)")
	public List<CalendarDetail> findAllByCalendarIdBetweenDate(Long calendarId,LocalDate startDate, LocalDate endDate);
	
	
	@Query("select c from CalendarDetail c where c.calendar.id = ?1")
	public List<CalendarDetail> findAllByCalendar(Long calendarId);
	
	@Query("select c from CalendarDetail c where c.raidPlan.id = ?1 and c.calendar.id = ?2")
	public List<CalendarDetail> findAllByRaidPlanAndCalendar(Long raidPlanId,Long calendarId);
	
	public List<CalendarDetail> findAllByRaidPlanAndCalendar(RaidPlan raidPlan,Calendar calendar);
	
	public List<CalendarDetail> findAllByRaidPlan(Long raidPlanId);
	
	//@Query("select c from CalendarDetail c where c.raidPlan.id = ?1")
	public List<CalendarDetail> findAllByRaidPlan(RaidPlan raidPlan);
	
	//@Query("select c from CalendarDetail c where c.calendar.id = ?1 and c.raidPlan.id = ?2")
	//public CalendarDetail findByCalendarAndRaidPlan(Long calendarId,Long raidPlanId);
	
}
