package com.guild.calendar.entity;

import java.util.ArrayList;
import java.util.List;

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
import javax.persistence.OneToMany;

import com.guild.calendar.constant.DiscordAuth;
import com.guild.calendar.dto.GuildForm;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@Entity
public class Guild extends BaseEntity{
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "Guild_SEQ_GENERATOR")
	@Column(name = "guild_id")
	private Long id;
	
	private String guildName;
	
	private String guildOwner;
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	
	@Enumerated(EnumType.STRING)
	private DiscordAuth discodeAuth; //인증 여부 
	
	
	
	public static Guild createGuild(String username, String guildName) {
		Guild guild = new Guild();
		
		guild.setGuildName(guildName);
		guild.setGuildOwner(username);
		guild.setDiscodeAuth(DiscordAuth.NOTAuthentication);
		
		return guild;
	}
	public void updateGuild(GuildForm guildForm) {
		this.guildName = guildForm.getGuildName();
		
		
	}
	
}
