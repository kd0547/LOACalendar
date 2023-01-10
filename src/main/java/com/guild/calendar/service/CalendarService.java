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
import com.guild.calendar.dto.ClendarUpdateForm;
import com.guild.calendar.entity.Calendar;
import com.guild.calendar.entity.Member;
import com.guild.calendar.entity.RaidPlan;
import com.guild.calendar.entity.CalendarDetail;
import com.guild.calendar.repository.CalendarRepository;
import com.guild.calendar.repository.MemberRepository;
import com.guild.calendar.repository.CalendarDetailRepository;
import com.guild.calendar.repository.RaidPlanRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarService {

	private final CalendarRepository calendarRepository;
	private final CalendarDetailRepository raidPlanGuildUserRepository;
	private final RaidPlanRepository raidPlanRepository;
	private final MemberRepository memberRepository;
	
	
	
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
		
		
		List<CalendarDetail> RaidPlanGuildUsers = raidPlanGuildUserRepository.selectByRaidStartDateWhereGuildName(guildName, startDate, endDate);
		
		for(CalendarDetail guildPlan :  RaidPlanGuildUsers) {
			
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
	public Long createCalendar(CalendarDto calendarDto) {
		Member member = memberRepository.findByEmail(calendarDto.getUsername());
		
		
		Calendar calendar = new Calendar.Builder()
							.member(member)
							.subject(calendarDto.getSubject())
							.build();
	
		Calendar saveCalendar = calendarRepository.save(calendar);
		return saveCalendar.getId();
	}

	@Transactional
	public void updateCalendar(String username, ClendarUpdateForm clendarUpdateForm) {
		Member member = memberRepository.findByEmail(username);
		
		Calendar calendar = calendarRepository.findByMemberAndId(member,clendarUpdateForm.getId());
	
		if(calendar == null) {
			throw new NullPointerException();
		}
		calendar.setSubject(clendarUpdateForm.getSubject());
	}
	
	
	public Calendar findByCalendarId(Long calendarId) {
		Optional<Calendar> findCalendar = calendarRepository.findById(calendarId);
		
		
		return findCalendar.get();
	}



	@Transactional
	public void deleteCalendar(String username, Long id) {
		
		Calendar findCalendar = calendarRepository.findByMemberIdAndcalendarId(username, id);
		
		if(findCalendar == null) {
			throw new NullPointerException();
		}
		
		List<CalendarDetail> raidPlanGuilds = raidPlanGuildUserRepository.findAllByCalendar(id);
		//List<RaidPlan> raidPlans = raidPlanRepository.findAllByCalendar(id);
		
		//raidPlanRepository.deleteAllInBatch(raidPlans);
		raidPlanGuildUserRepository.deleteAllInBatch(raidPlanGuilds);	
		
		calendarRepository.delete(findCalendar);
		
		
	}

	public List<CalendarDto> findByCalendar(String username) {
		Member member = memberRepository.findByEmail(username);
		
		System.out.println(member);
		
		List<Calendar> calendars = calendarRepository.findAllByMember(member);
		
		System.out.println(calendars);
		
		List<CalendarDto> calendarDtos = new ArrayList<CalendarDto>();
		
		for(Calendar calendar : calendars) {
			CalendarDto calendarDto = new CalendarDto();
			calendarDto.setId(calendar.getId());
			calendarDto.setSubject(calendar.getSubject());
			calendarDto.setUsername(username);
			calendarDto.setCreateDate(calendar.getRegTime().toLocalDate());
			
			calendarDtos.add(calendarDto);
		}
		
		
		return calendarDtos;
	}


	
	
	
	
}
