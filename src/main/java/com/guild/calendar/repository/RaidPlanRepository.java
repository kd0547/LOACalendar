package com.guild.calendar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.guild.calendar.entity.RaidPlan;

public interface RaidPlanRepository extends JpaRepository<RaidPlan, Long>{
	
	//public List<RaidPlan> findAllByCalendar(Long calendarId);
	
	
	public Optional<RaidPlan> findById(Long raidId);
	
	@Query("select r from RaidPlan r where r.id= ?1 and r.calendar.id = ?2")
	public RaidPlan findByIdAndCalendar(Long raidPlanId, Long calendarId);
	
	
	
}
