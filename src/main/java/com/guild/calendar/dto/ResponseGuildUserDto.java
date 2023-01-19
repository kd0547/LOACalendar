package com.guild.calendar.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class ResponseGuildUserDto {
	private Long id;
	
	private String GuildName;
	
	private List<GuildUserDto> guildUserDtos;
}
