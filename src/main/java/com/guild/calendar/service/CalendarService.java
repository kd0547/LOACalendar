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
	private final CalendarDetailRepository calendarDetailRepository;
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
		
		
		List<CalendarDetail> RaidPlanGuildUsers = calendarDetailRepository.selectByRaidStartDateWhereGuildName(guildName, startDate, endDate);
		
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
	public void updateCalendar(String username,Long calendarId ,ClendarUpdateForm clendarUpdateForm) {
		//Member member = memberRepository.findByEmail(username);
		
		Calendar calendar = calendarRepository.findByIdAndOwner(calendarId,username);
		
		if(calendar == null) {
			throw new NullPointerException("-190");
		}
		
		
		calendar.setSubject(clendarUpdateForm.getSubject());
	}
	
	
	public Calendar findByCalendarId(Long calendarId) {
		Optional<Calendar> findCalendar = calendarRepository.findById(calendarId);
		
		
		return findCalendar.get();
	}


	/**
	 * 캘린더와 계획을 모두 삭제합니다. 
	 * @param username
	 * @param Calendarid
	 */
	@Transactional
	public void deleteCalendar(String username, Long Calendarid) {
		
		Calendar calendar = calendarRepository.findByIdAndOwner(Calendarid,username);
		
		if(calendar == null) {
			throw new NullPointerException("-190");
		}
		
		List<CalendarDetail> calendarDetails = calendarDetailRepository.findAllByCalendar(Calendarid);
		List<RaidPlan> raidPlans = raidPlanRepository.findAllByCalendar(Calendarid);
		
		raidPlanRepository.deleteAllInBatch(raidPlans);
		calendarDetailRepository.deleteAllInBatch(calendarDetails);	
		
		calendarRepository.delete(calendar);
	}
	/**
	 * 캘린더 조회 메서드 
	 * @param username
	 * @return
	 */
	public List<CalendarDto> findByCalendar(String username) {
		//Member member = memberRepository.findByEmail(username);
		
		//List<Calendar> calendars = calendarRepository.findAllByMember(member);
		List<Calendar> calendars = calendarRepository.findByOwner(username);
		List<CalendarDto> calendarDtos = new ArrayList<CalendarDto>();
		
		for(Calendar calendar : calendars) {
			CalendarDto calendarDto = new CalendarDto();
			calendarDto.setId(calendar.getId());
			calendarDto.setSubject(calendar.getSubject());
			//calendarDto.setUsername(username);
			calendarDto.setCreateDate(calendar.getRegTime().toLocalDate());
			
			calendarDtos.add(calendarDto);
		}
		
		
		return calendarDtos;
	}


	
	
	
	
}
