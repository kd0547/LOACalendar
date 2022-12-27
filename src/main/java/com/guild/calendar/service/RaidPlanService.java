package com.guild.calendar.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guild.calendar.Annotation.ServiceTimeLogger;
import com.guild.calendar.dto.GuildUserDto;
import com.guild.calendar.dto.RaidPlanRequestDto;
import com.guild.calendar.entity.Calendar;
import com.guild.calendar.entity.GuildUser;
import com.guild.calendar.entity.RaidPlan;
import com.guild.calendar.entity.RaidPlanGuildUser;
import com.guild.calendar.repository.GuildUserRepository;
import com.guild.calendar.repository.RaidPlanGuildUserRepository;
import com.guild.calendar.repository.RaidPlanRepository;
import com.guild.calendar.sub.RequestTime;

@Service
public class RaidPlanService {
	
	private final RaidPlanRepository raidPlanRepository;
	private final RaidPlanGuildUserService raidPlanGuildUserService;
	private final CalendarService calendarService;
	private final RequestTime requestTime;
	
	
	@Autowired
	public RaidPlanService(RaidPlanRepository raidPlanRepository,RaidPlanGuildUserService raidPlanGuildUserService,CalendarService calendarService,RequestTime requestTime) {
		this.raidPlanRepository = raidPlanRepository;
		this.raidPlanGuildUserService = raidPlanGuildUserService;
		this.calendarService = calendarService;
		this.requestTime = requestTime;
	}
	
	
	/**
	 * 
	 * @param planRequestDto
	 * @return Long 엔티티의 id를 반환한다.
	 */
	@Transactional
	@ServiceTimeLogger
	public void createRaidPlan(RaidPlanRequestDto planRequestDto) {
		requestTime.logTest("createRaidPlan 서비스 실행");
		
		RaidPlan raidPlan = new RaidPlan();
		List<GuildUserDto> guildUsers = planRequestDto.getGuildUser();
		
		raidPlan.setLegionRaid(planRequestDto.getRaid());
		raidPlan.setRaidStartDateTime(planRequestDto.CreateRaidDateTime());
		
		RaidPlan saveRaidPlan = raidPlanRepository.save(raidPlan);
		Calendar calendar = calendarService.findByCalendarId(planRequestDto.getCalendarId());
		
		
		
		raidPlanGuildUserService.saveRaidPlanGuildUser(calendar,saveRaidPlan, guildUsers);
		
	}
	
	
}
