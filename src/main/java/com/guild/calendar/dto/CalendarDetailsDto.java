package com.guild.calendar.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter

public class CalendarDetailsDto {
	
	private Long id;//
	
	private String yearMonth;//요청한 년월
	
	private List<RaidPlanDto> raidPlanDto;
	
}
