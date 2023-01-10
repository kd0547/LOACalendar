package com.guild.calendar.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.guild.calendar.entity.Calendar;
import com.guild.calendar.entity.Member;

public interface CalendarRepository extends JpaRepository<Calendar,Long>{
	
	public List<Calendar> findAllByMemberId(String memberId);
	
	@Deprecated
	@Query(value = "select * from calendar c where c.member_id = ?1 and c.calendar_id = ?2",nativeQuery = true)
	public Calendar findByMemberIdAndcalendarId(String memberId, Long calendarId);
	
	public Calendar findByMemberAndId(Member memberId, Long calendarId);
	
	public List<Calendar> findAllByMember(Member memberId);
	

	public Optional<Calendar> findById(Long calendarId);

}
