package com.guild.calendar.service;

import java.security.acl.Owner;
import java.util.ArrayList;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.guild.calendar.Annotation.ServiceTimeLogger;
import com.guild.calendar.dto.GuildUserDto;
import com.guild.calendar.dto.RaidPlanRequestDto;
import com.guild.calendar.dto.RaidPlanResponseDto;
import com.guild.calendar.entity.Calendar;
import com.guild.calendar.entity.CalendarDetail;
import com.guild.calendar.entity.GuildUser;
import com.guild.calendar.entity.Member;
import com.guild.calendar.entity.RaidPlan;
import com.guild.calendar.repository.CalendarRepository;
import com.guild.calendar.repository.GuildUserRepository;
import com.guild.calendar.repository.MemberRepository;
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
	private final MemberRepository memberRepository;
	private final RequestTime requestTime;
	
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
				CalendarDetail raidPlanGuildUser = new CalendarDetail();
				
				raidPlanGuildUser.setCalendar(calendar);
				raidPlanGuildUser.setGuildUser(findGuildUser);
				raidPlanGuildUser.setRaidPlan(raidPlan);
				raidPlanGuildUsers.add(raidPlanGuildUser);
				//raidPlanGuildUserRepository.save(raidPlanGuildUser);
	
			}
			
		}
		//System.out.println("실행 테스트");
		calendarDetailRepository.saveAll(raidPlanGuildUsers);
		
		
	}

	public void updateRaidPlanGuildUser(RaidPlanRequestDto planRequestDto) {
		
		
	}
	
	/**
	 * 
	 * @param username
	 * @param planId
	 * @param calendarId
	 * @param planRequestDto
	 * @throws IllegalAccessException 
	 */
	@Transactional
	public void updateRaidPlanGuildUser(String username, Long planId, Long calendarId, RaidPlanRequestDto planRequestDto) {
		Optional<RaidPlan> findRaidPlan = raidPlanRepository.findById(planId);
		RaidPlan raidPlan = findRaidPlan.get();
		Long findCalendar = raidPlan.getCalendar().getId();
		String owner = raidPlan.getOwner();
		
		if(calendarId != findCalendar || !owner.equals(username)) {
			throw new IllegalArgumentException("잘못된 접근입니다.");
		}
		//System.out.println(planRequestDto.toString());
		raidPlan.patchUpdateRaidPlan(planRequestDto);
		
		if(!planRequestDto.getGuildUser().isEmpty()) {
			//변경 후 데이터 
			List<GuildUserDto> guildUserDtos = planRequestDto.getGuildUser();
			
		 	List<CalendarDetail> calendarDetails = calendarDetailRepository.findAllByRaidPlan(raidPlan);
		 	//변경 전 데이터
		 	List<GuildUser> guildUsers = new ArrayList<GuildUser>();
		 	
		 	for(CalendarDetail calendarDetail : calendarDetails) {
		 		GuildUser guildUser = calendarDetail.getGuildUser();
		 		guildUsers.add(guildUser);

		 	}
		 	
		 	int reqeustGuildUserDtoSize = guildUserDtos.size();
		 	int SaveGuildUserSize = guildUsers.size();
		 
		 	if(reqeustGuildUserDtoSize != SaveGuildUserSize) {
		 
		 		throw new IllegalArgumentException("계획된 유저 수보다 많거나 적습니다.");
		 	}

		 	/**
		 	 * 
		 	 */
		 	int i = 0;
		 	while(i < SaveGuildUserSize) { 		
		 		for(int j=0;j < reqeustGuildUserDtoSize;j++) {
		 			Long old = guildUsers.get(i).getId();
		 			Long _new = guildUserDtos.get(j).getId();
		 			
		 			if(_new == null) {
		 				throw new IllegalArgumentException("null값은 사용할 수 없습니다.");
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
	
	}

	/**
	 * 
	 * @param planRequestDto
	 * @return Long 엔티티의 id를 반환한다.
	 */
	@Transactional
	@ServiceTimeLogger
	public void createRaidPlan(String username,Long calendarId,RaidPlanRequestDto planRequestDto) {
		requestTime.logTest("createRaidPlan 서비스 실행");
		
		Member findMember = memberRepository.findByEmail(username);
		
		Calendar calendar = calendarRepository.findByMemberAndId(findMember, calendarId);
		
		if(calendar == null) {
			throw new NullPointerException("캘린더가 없습니다.");
		}
		
		RaidPlan raidPlan = new RaidPlan();

		raidPlan.setLegionRaid(planRequestDto.getRaid());
		raidPlan.setRaidStartDate(planRequestDto.getStartDate());
		raidPlan.setRaidStartTime(planRequestDto.getStartTime());
		
		
		RaidPlan saveRaidPlan = raidPlanRepository.save(raidPlan);
		
		List<GuildUserDto> guildUsers = planRequestDto.getGuildUser();
		
		//calendarDetailRepository.saveRaidPlanGuildUser(calendar,saveRaidPlan, guildUsers);
		
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
			throw new IllegalArgumentException("잘못된 접근입니다.");
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
			System.out.println("test2");
			new IllegalAccessException("잘못된 접근입니다.");
		}
		List<CalendarDetail> calendarDetails = calendarDetailRepository.findAllByRaidPlan(planId);
		
		
		calendarDetailRepository.deleteAll(calendarDetails);
		raidPlanRepository.delete(findRaidPlan);
		
	}
	
	
}
