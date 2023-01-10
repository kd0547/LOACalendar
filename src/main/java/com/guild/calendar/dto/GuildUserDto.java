package com.guild.calendar.dto;


import com.guild.calendar.constant.LoaClass;
import com.guild.calendar.entity.GuildUser;

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
	
	public static GuildUserDto createGuildUserDto(GuildUser guildUser) {
		GuildUserDto guildUserDto = new GuildUserDto();
		guildUserDto.setId(guildUser.getId());
		guildUserDto.setLoaClass(guildUser.getLoaClass());
		guildUserDto.setUsername(guildUser.getUsername());
		guildUserDto.setLevel(guildUser.getLevel());
		
		return guildUserDto;
	}
}
