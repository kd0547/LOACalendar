package com.guild.calendar.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guild.calendar.dto.GuildUserDto;
import com.guild.calendar.entity.Calendar;
import com.guild.calendar.entity.GuildUser;
import com.guild.calendar.entity.RaidPlan;
import com.guild.calendar.entity.RaidPlanGuildUser;
import com.guild.calendar.repository.GuildUserRepository;
import com.guild.calendar.repository.RaidPlanGuildUserRepository;

@Service
public class RaidPlanGuildUserService {
	private RaidPlanGuildUserRepository raidPlanGuildUserRepository;
	private GuildUserRepository guildUserRepository;
	
	
	@Autowired
	public RaidPlanGuildUserService(RaidPlanGuildUserRepository raidPlanGuildUserRepository,GuildUserRepository guildUserRepository) {
		this.raidPlanGuildUserRepository = raidPlanGuildUserRepository;
		this.guildUserRepository = guildUserRepository;
		
	}
	
	/**
	 * 
	 * @param calendar 
	 * @param raidPlan
	 * @param guildUserDtos
	 */
	public void saveRaidPlanGuildUser(Calendar calendar, RaidPlan raidPlan ,List<GuildUserDto> guildUserDtos) {
		List<RaidPlanGuildUser> raidPlanGuildUsers = new ArrayList<RaidPlanGuildUser>();
		
		for(GuildUserDto guildUser : guildUserDtos) {
			GuildUser findGuildUser = guildUserRepository.findByUsername(guildUser.getUsername());
			
			if(findGuildUser != null) {
				RaidPlanGuildUser raidPlanGuildUser = new RaidPlanGuildUser();
				
				raidPlanGuildUser.setCalendar(calendar);
				raidPlanGuildUser.setGuildUser(findGuildUser);
				raidPlanGuildUser.setRaidPlan(raidPlan);
				raidPlanGuildUser.setRaidStartDateTime(raidPlan.getRaidStartDateTime());
				
				raidPlanGuildUsers.add(raidPlanGuildUser);
				//raidPlanGuildUserRepository.save(raidPlanGuildUser);
	
			}
			
		}
		//System.out.println("실행 테스트");
		raidPlanGuildUserRepository.saveAll(raidPlanGuildUsers);
		
		
	}
	
	
	
}
