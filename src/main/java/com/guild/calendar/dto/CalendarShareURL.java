package com.guild.calendar.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CalendarShareURL {
	
	private Long calendarId;
	
	private Long MemberId;
	
	private LocalDate expiredTime;
}
