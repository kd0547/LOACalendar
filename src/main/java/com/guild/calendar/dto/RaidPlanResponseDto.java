package com.guild.calendar.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.guild.calendar.constant.LegionRaid;
import com.guild.calendar.entity.CalendarDetail;
import com.guild.calendar.entity.RaidPlan;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class RaidPlanResponseDto {
	
	private Long planId;
	private LocalDate startDate;
	private LocalTime startTime;
	private LegionRaid raid;
	private List<GuildUserDto> guildUser;

	public LocalDateTime CreateRaidDateTime() {

		if(startDate != null && startTime != null) {
			return LocalDateTime.of(this.startDate,this.startTime);
		} else {
			throw new NullPointerException("startDate : "+startDate+", "+"startTime : "+startTime+"을 확인해주세요");
		}
	}

}
