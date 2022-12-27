package com.guild.calendar.dto;

import java.time.LocalDate;

import com.guild.calendar.entity.Member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class CalendarDto {
	
	private Long id;
	
	private String subject;
	
	private String username; //member_id;
	
	private LocalDate createDate;
	
	private String guildName;

	
	
	
}
