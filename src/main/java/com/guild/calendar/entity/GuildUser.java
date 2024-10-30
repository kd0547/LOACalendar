package com.guild.calendar.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.guild.calendar.constant.LoaClass;
import com.guild.calendar.dto.GuildUserDto;

import com.guild.calendar.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@Entity
public class GuildUser extends BaseEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "GuildUser_SEQ_GENERATOR")
	@Column(name = "guild_user_id")
	private Long id;
	
	@JoinColumn(name = "guild_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Guild guild;
	
	private String memberEmail;
	
	private String guildName;
	
	@Enumerated(EnumType.STRING)
	private LoaClass loaClass; 
	
	private int level;
	
	
	@Column(unique = true)
	private String username;

	public static GuildUser createGuildUser(GuildUserDto guildUserDto) {
		GuildUser guildUser = new GuildUser();
		
		guildUser.setLevel(guildUserDto.getLevel());
		guildUser.setUsername(guildUserDto.getUsername());
		guildUser.setLoaClass(guildUserDto.getLoaClass());
		
		
		return guildUser;
	}
	public void updateGuildUser(GuildUserDto guildUserDto) {
		
		if(guildUserDto.getLevel() != 0) {
			this.level = guildUserDto.getLevel();
		}
		
		if(guildUserDto.getUsername() != null) {
			this.username = guildUserDto.getUsername();
		}
		
		if(guildUserDto.getLoaClass() != LoaClass.NONE) {
			this.loaClass = guildUserDto.getLoaClass();
		}

	}
}
