package com.guild.calendar.entity;



import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import com.guild.calendar.constant.LoaClass;

import com.guild.calendar.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Setter @Getter @ToString
public class GuildUser extends BaseEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "GuildUser_SEQ_GENERATOR")
	@Column(name = "guild_user_id")
	private Long id;

	private Guild guild;
	
	private String memberEmail;
	
	private String guildName;
	
	@Enumerated(EnumType.STRING)
	private LoaClass loaClass; 
	
	private int level;
}
