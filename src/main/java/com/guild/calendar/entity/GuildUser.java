package com.guild.calendar.entity;



import jakarta.persistence.*;

import com.guild.calendar.constant.LoaClass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter @Getter @ToString
public class GuildUser {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guild_id")
	private Guild guild;
	
	private String guildUserEmail;

	@Enumerated(EnumType.STRING)
	private LoaClass loaClass; 
	
	private int level;
}
