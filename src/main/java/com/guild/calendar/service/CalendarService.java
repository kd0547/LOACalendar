package com.guild.calendar.service;

import java.time.LocalDate;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guild.calendar.Exception.NotFoundException;
import com.guild.calendar.dto.CalendarDto;
import com.guild.calendar.dto.CalendarShortInfoDto;
import com.guild.calendar.entity.Calendar;
import com.guild.calendar.entity.RaidPlanGuildUser;
import com.guild.calendar.repository.CalendarRepository;
import com.guild.calendar.repository.RaidPlanGuildUserRepository;

@Service

public class CalendarService {

	private final CalendarRepository calendarRepository;
	private final RaidPlanGuildUserRepository raidPlanGuildUserRepository;
	
	@Autowired
	public CalendarService(RaidPlanGuildUserRepository raidPlanGuildUserRepository, CalendarRepository calendarRepository) {
		this.raidPlanGuildUserRepository = raidPlanGuildUserRepository;
		this.calendarRepository = calendarRepository;
		
	}
	
	
	
	/**
	 * 
	 * @param guildName
	 * @param startDate
	 */
	public void calendarYearMonth(String guildName,LocalDateTime startDate) {
		
		LocalDateTime endDate;
		
		
		YearMonth test = YearMonth.from(startDate);
		LocalDate month = test.atEndOfMonth();
		
		LocalTime endTime =  LocalTime.of(23, 59, 59);
		
		endDate = LocalDateTime.of(month,endTime);
		
		
		List<RaidPlanGuildUser> RaidPlanGuildUsers = raidPlanGuildUserRepository.selectByRaidStartDateWhereGuildName(guildName, startDate, endDate);
		
		for(RaidPlanGuildUser guildPlan :  RaidPlanGuildUsers) {
			
		}
		
	}
	
	public List<CalendarShortInfoDto> viewCalendar(String owner) {
		
		List<Calendar> calendars = calendarRepository.findAllByMemberId(owner);
		List<CalendarShortInfoDto> calendarShortInfoDto = new ArrayList<CalendarShortInfoDto>();
		
		for(Calendar calendar : calendars) {
			calendarShortInfoDto.add(CalendarShortInfoDto.createCalendarShortInfoDto(calendar));
		}
		
		
		return calendarShortInfoDto;
	}
	
	/**
	 * 
	 * @param calendarDto
	 */
	public void createCalendar(CalendarDto calendarDto) {
		
		Calendar calendar = new Calendar.Builder()
							//.member(calendarDto.getUsername())
							.subject(calendarDto.getSubject())
							.build();
	
		calendarRepository.save(calendar);
		
	}


	@Transactional
	public void updateCalendar(CalendarDto calendarDto) {
		
		Calendar calendar = calendarRepository.findByMemberIdAndcalendarId(calendarDto.getUsername(),calendarDto.getId());
		
		if(calendar == null) {
			throw new NotFoundException();
		}
		
		calendar.setSubject(calendarDto.getSubject());
		
		
	}
	public Calendar findByCalendarId(Long calendarId) {
		Optional<Calendar> findCalendar = calendarRepository.findById(calendarId);
		
		
		return findCalendar.get();
	}
	
	
	
}
