package com.guild.calendar.service;



import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.guild.calendar.Annotation.ServiceTimeLogger;
import com.guild.calendar.dto.CalendarDetailsDto;
import com.guild.calendar.dto.GuildUserDto;
import com.guild.calendar.dto.RaidPlanDto;
import com.guild.calendar.dto.RaidPlanRequestDto;
import com.guild.calendar.dto.RaidPlanResponseDto;
import com.guild.calendar.entity.Calendar;
import com.guild.calendar.entity.CalendarDetail;
import com.guild.calendar.entity.GuildUser;
import com.guild.calendar.entity.RaidPlan;
import com.guild.calendar.repository.CalendarRepository;
import com.guild.calendar.repository.GuildUserRepository;
import com.guild.calendar.repository.CalendarDetailRepository;
import com.guild.calendar.repository.RaidPlanRepository;
import com.guild.calendar.sub.RequestTime;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarDetailService {
	
	
	private final CalendarDetailRepository calendarDetailRepository;
	private final GuildUserRepository guildUserRepository;
	private final RaidPlanRepository raidPlanRepository;
	private final CalendarRepository calendarRepository;
	private final RequestTime requestTime;
	
	

	
	
	/**
	 * 레이드 계획을 수정합니다. 
	 * @param username
	 * @param planId
	 * @param calendarId
	 * @param planRequestDto
	 * @throws IllegalAccessException 
	 */
	@Transactional
	public Long updateRaidPlanGuildUser(String username, Long planId, Long calendarId, RaidPlanRequestDto planRequestDto) {
		
		//요청에서 calendarID가 본인 소유가 맞는지 검사한다. 
		Calendar calendar = calendarRepository.findByIdAndOwner(calendarId, username);
		
		if(calendar == null) {
			throw new IllegalStateException("-190");
		}
		
		RaidPlan raidPlan = raidPlanRepository.findByIdAndCalendar(planId,calendarId);
		
		if(raidPlan == null) {
			throw new IllegalStateException("-210");
		}
		
		//레이드 계획을 수정합니다.
		raidPlan.patchUpdateRaidPlan(planRequestDto);
		
		// 참여 길드원 중에서 변경할 데이터가 있으면 실행됩니다. 
		if(!planRequestDto.getGuildUser().isEmpty()) {
			//길드원 수정을 요청한 데이터입니다.  
			List<GuildUserDto> guildUserDtos = planRequestDto.getGuildUser();
			
			int requestRaidStartHeadCount = guildUserDtos.size();
			int RaidStartHeadCount = raidPlan.getLegionRaid().getHeadCount();
			
			// 레이드 시작 가능 인원과 맞는지 검사합니다. 
			if(requestRaidStartHeadCount != RaidStartHeadCount) {
				throw new IllegalStateException("-211");
			}
			
			//기존 레이드 계획을 가져옵니다.
			List<CalendarDetail> calendarDetails = calendarDetailRepository.findAllByRaidPlan(raidPlan);
		 	List<GuildUser> guildUsers = new ArrayList<GuildUser>();
		 	
		 	//기존 레이드 계획에서 참여 인원을 가져옵니다. 
		 	for(CalendarDetail calendarDetail : calendarDetails) {
		 		GuildUser guildUser = calendarDetail.getGuildUser();
		 		guildUsers.add(guildUser);
		 	}
		 	//
		 	int reqeustGuildUserDtoSize = guildUserDtos.size();
		 	int SaveGuildUserSize = guildUsers.size();
		 
		 	
		 	int i = 0;
		 	while(i < SaveGuildUserSize) { 		
		 		for(int j=0;j < reqeustGuildUserDtoSize;j++) {
		 			Long old = guildUsers.get(i).getId();
		 			Long _new = guildUserDtos.get(j).getId();
		 			
		 			if(_new == null) {
		 				throw new IllegalArgumentException("-212");
		 			}
		 			
		 			
		 			if(old == _new) {
		 				guildUsers.remove(i);
		 				guildUserDtos.remove(j);
		 				
		 				SaveGuildUserSize = guildUsers.size();
		 				reqeustGuildUserDtoSize = guildUserDtos.size();
		 				
		 				i = 0;
		 			} 
		 		
		 		}
		 		i++;		 		
		 	}
		 	/*
		 	 * 2023-01-19 추가
			 * 여기서 개선해야할 점은 
			 * 데이터를 저장할 때 guildUserRepository.findById() 메서드가 실행되는 것입니다.
			 * 
			 * 요청 값을 검사하는 단계에서 guildUserRepository.findById() 메서드를 실행하면 데이터 검사와 저장(또는 변경) 단계가 분리되지만 
			 * 
			 * Optional<GuildUser> findGuildUser = guildUserRepository.findById(temp);
			 * GuildUser saveguildUser = findGuildUser.get();
			 * 
			 * 이 두줄 때문에 저장에서 추가로 검사 코드(데이터가 없을 시 Exception을 발생시키는 코드)를 추가해야한다. 
			 * 
			 * if(_new == null) {
		 				throw new IllegalArgumentException("-212");
		 			}
			 * 이 코드도 마찬가지 
			 * 
			 */
		 	//변경 내용을 저장합니다. 
		 	int j=0;
		 	for(GuildUser guildUser : guildUsers) {
	 			Long after = guildUser.getId();
	 			
	 			Long temp = guildUserDtos.get(j).getId();
	 			
	 			for(CalendarDetail calendarDetail : calendarDetails) {
			 		Long before = calendarDetail.getGuildUser().getId();
			 		
			 		if(before == after) {
			 			Optional<GuildUser> findGuildUser = guildUserRepository.findById(temp);
			 			GuildUser saveguildUser = findGuildUser.get();
			 			
			 			calendarDetail.setGuildUser(saveguildUser);
			 		}

			 	}
	 			j++;
	 		}
		 	 	
		}
		
		return raidPlan.getId();
		
	}

	/**
	 * 레이드 계획을 저장하는 메서드 RaidPlan 을 먼저 저장하고 RaidPlan.id로 CalendarDetail을 저장한다.
	 * CalendarDetail을 저장하기 전에 길드 유저를 검색해 GuildUser Entity를 CalendarDetail에 설정한다. 
	 * 
	 * @param planRequestDto
	 * @return Long 엔티티의 id를 반환한다.
	 */
	@Transactional
	@ServiceTimeLogger
	public Long createRaidPlan(String username,Long calendarId,RaidPlanRequestDto planRequestDto) {
		requestTime.logTest("createRaidPlan 서비스 실행");
		
		Calendar calendar = calendarRepository.findByIdAndOwner(calendarId,username);
		
		if(calendar == null) {
			
			throw new IllegalStateException("-190");
		}
		
		RaidPlan raidPlan = new RaidPlan();
		raidPlan.setLegionRaid(planRequestDto.getRaid());
		raidPlan.setRaidStartDate(planRequestDto.getStartDate());
		raidPlan.setRaidStartTime(planRequestDto.getStartTime());
		raidPlan.setCalendar(calendar);
		raidPlan.setOwner(username);
		
		//레이드 계획을 저장합니다. 
		RaidPlan saveRaidPlan = raidPlanRepository.save(raidPlan);
		
		List<GuildUserDto> guildUserDtos = planRequestDto.getGuildUser();
		
		
		
		saveRaidPlanGuildUser(calendar,saveRaidPlan, guildUserDtos);
		
		return saveRaidPlan.getId();
		
	}
	//**버그**
	/*
	 * 1. CalendarDetail에서 오류가 발생해도 레이드 계획은 DB에 저장되는 문제
	 * 
	 */
	
	/**
	 * 
	 * @param calendar 
	 * @param raidPlan
	 * @param guildUserDtos
	 */
	public void saveRaidPlanGuildUser(Calendar calendar, RaidPlan raidPlan ,List<GuildUserDto> guildUserDtos) {
		List<CalendarDetail> raidPlanGuildUsers = new ArrayList<CalendarDetail>();
		
		for(GuildUserDto guildUser : guildUserDtos) {
			GuildUser findGuildUser = guildUserRepository.findByUsername(guildUser.getUsername());
			
			if(findGuildUser != null) {
				CalendarDetail calendarDetail = new CalendarDetail();
				
				calendarDetail.setCalendar(calendar);
				calendarDetail.setGuildUser(findGuildUser);
				calendarDetail.setRaidPlan(raidPlan);
				calendarDetail.setStartDate(raidPlan.getRaidStartDate());
				
				
				raidPlanGuildUsers.add(calendarDetail);
				//raidPlanGuildUserRepository.save(raidPlanGuildUser);
	
			}
			
		}
		//System.out.println("실행 테스트");
		calendarDetailRepository.saveAll(raidPlanGuildUsers);
		
		
	}

	/**
	 * 
	 * 
	 * @param username
	 * @param planId
	 * @param calendarId
	 * @return
	 */
	public RaidPlanResponseDto findRaidPlan(String username, Long planId, Long calendarId) {
		//Calendar calendar = calendarRepository.findByMemberIdAndcalendarId(username, calendarId);
		Optional<Calendar> findOptional = calendarRepository.findById(calendarId);
		Calendar calendar = findOptional.get();
		
		if(!calendar.getOwner().equals(username)) {
			throw new IllegalArgumentException("-210");
		}
		RaidPlan raidPlan = raidPlanRepository.findByIdAndCalendar(planId,calendar.getId());
		List<CalendarDetail> calendarDetails = calendarDetailRepository.findAllByRaidPlanAndCalendar(planId, calendarId);
		
		RaidPlanResponseDto raidPlanResponseDto = new RaidPlanResponseDto();
		raidPlanResponseDto.setRaidPlan(raidPlan);
			
		for(CalendarDetail calendarDetail : calendarDetails) {
			GuildUser guildUser = calendarDetail.getGuildUser();
			GuildUserDto guildUserDto = GuildUserDto.createGuildUserDto(guildUser);
			raidPlanResponseDto.addGuildUser(guildUserDto);
		}

		return raidPlanResponseDto;
	}

	public void deleteRaidPlan(String username, Long planId, Long calendarId) {
		Optional<RaidPlan> findOptional = raidPlanRepository.findById(planId);
		RaidPlan findRaidPlan = findOptional.get();
		
		
		/*
		 *	요청값과 DB 값을 비교
		 */
		if(!findRaidPlan.getOwner().equals(username) || findRaidPlan.getCalendar().getId() != calendarId) {
			
			new IllegalArgumentException("-210");
		}
		List<CalendarDetail> calendarDetails = calendarDetailRepository.findAllByRaidPlan(planId);
		
		
		calendarDetailRepository.deleteAll(calendarDetails);
		raidPlanRepository.delete(findRaidPlan);
		
	}
	/**
	 * 공유 URL로 캘린더를 조회 시 사용하는 메서드 
	 * 
	 * @param requestYearMonth
	 * @param calendarId
	 * @param memberId
	 * @return
	 */
	/*
	 * 버그
	 * 캘린더 조회 시 Between을 사용해서 2023-01-1 ~ 2023-01-31 사이의 값을 조회한다. 
	 * 여기서 1, 31일에 해당하는 계획은 조회되지 않는다.
	 * -> 해결했음
	 */
	public CalendarDetailsDto findCalendarAll(LocalDate requestYearMonth,Long calendarId, Long memberId) {
		
		LocalDate firstDate = requestYearMonth.withDayOfMonth(1);
		LocalDate lastDate = requestYearMonth.withDayOfMonth(requestYearMonth.lengthOfMonth());
		
		
		
		List<CalendarDetail> calendarDetails = calendarDetailRepository.findAllByCalendarIdBetweenDate(calendarId,firstDate,lastDate);
		List<RaidPlanDto> raidPlanDtos = new ArrayList<RaidPlanDto>();
		CalendarDetailsDto calendarDetailsDto = new CalendarDetailsDto();
		RaidPlan raidPlan = new RaidPlan();
		
		
		
		for(CalendarDetail calendarDetail : calendarDetails) {
	
			if(raidPlan.getId() == calendarDetail.getRaidPlan().getId()) {
				raidPlan = calendarDetail.getRaidPlan();
			} else {
				
				RaidPlan findraidPlan = calendarDetail.getRaidPlan();
				RaidPlanDto raidPlanDto = new RaidPlanDto();
				raidPlanDto.setId(findraidPlan.getId());
				raidPlanDto.setLegionRaid(findraidPlan.getLegionRaid());
				raidPlanDto.setRaidStartDate(findraidPlan.getRaidStartDate());
				raidPlanDto.setRaidStartTime(findraidPlan.getRaidStartTime());
				
				raidPlanDtos.add(raidPlanDto);
				
				raidPlan = findraidPlan;
			}
 			
			
			
		}
		calendarDetailsDto.setId(calendarId);
		//calendarDetailsDto.setYearMonth(requestYearMonth);
		calendarDetailsDto.setRaidPlanDto(raidPlanDtos);
		
		return calendarDetailsDto;
		
	}
	
	
	
}
