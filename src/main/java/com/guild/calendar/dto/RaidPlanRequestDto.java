package com.guild.calendar.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.guild.calendar.constant.LegionRaid;

import lombok.Setter;
import lombok.Getter;
import lombok.ToString;


@ToString
@Getter
@Setter
public class RaidPlanRequestDto {
	
	private Long calendarId;
	
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
	
}
