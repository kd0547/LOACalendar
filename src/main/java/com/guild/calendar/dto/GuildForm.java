package com.guild.calendar.dto;

import com.guild.calendar.constant.DiscordAuth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class GuildForm {
	
	private Long id;
	
	private String guildName;
	
	private String guildOwner;
	
	private DiscordAuth Auth;
}
