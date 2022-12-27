package com.guild.calendar.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import com.guild.calendar.constant.LegionRaid;
import com.guild.calendar.constant.LoaClass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@Setter
public class GuildUserDto {
	
	private Long id;
	
	private LoaClass loaClass;

	private String username;
	
	private int level;
}
