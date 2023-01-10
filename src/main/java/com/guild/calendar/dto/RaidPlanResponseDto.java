package com.guild.calendar.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.guild.calendar.constant.LegionRaid;
import com.guild.calendar.entity.RaidPlan;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class RaidPlanResponseDto {
	
	private Long planId;
	
	private LocalDate startDate;
	
	private LocalTime startTime;
	
	private LegionRaid raid;
	
	private List<GuildUserDto> guildUser = new ArrayList<GuildUserDto>();
	
	
	public LocalDateTime CreateRaidDateTime() {
		
		if(startDate != null && startTime != null) {
			return LocalDateTime.of(this.startDate,this.startTime);
		} else {
			throw new NullPointerException("startDate : "+startDate+", "+"startTime : "+startTime+"을 확인해주세요");
		}
	}
	
	public void addGuildUser(GuildUserDto guildUserDto) {
		guildUser.add(guildUserDto);
	}
	
	public void setRaidPlan(RaidPlan raidPlan) {
		this.planId = raidPlan.getId();
		this.raid = raidPlan.getLegionRaid();
		this.startDate = raidPlan.getRaidStartDate();
		this.startTime = raidPlan.getRaidStartTime();
		
	}
	
}
