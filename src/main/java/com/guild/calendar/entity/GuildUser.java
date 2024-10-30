package com.guild.calendar.entity;



import jakarta.persistence.*;

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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guild_id")
	private Guild guild;
	
	private String guildUserEmail;

	@Enumerated(EnumType.STRING)
	private LoaClass loaClass; 
	
	private int level;
}
