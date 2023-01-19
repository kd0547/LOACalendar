package com.guild.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.guild.calendar.constant.LegionRaid;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter

public class RaidPlanDto {
	private Long id;
	
	private LegionRaid legionRaid;
	
	private LocalDate raidStartDate;
	
	private LocalTime raidStartTime;
	
	
}
