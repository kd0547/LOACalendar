package com.guild.calendar.entity;

import jakarta.persistence.*;

import com.guild.calendar.constant.DiscordAuth;
import com.guild.calendar.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString @Getter @Setter
@Entity
public class Guild extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "Guild_SEQ_GENERATOR")
	@Column(name = "guild_id")
	private Long id;

	private String guildName;


	private List<GuildUser> guildUsers = new ArrayList<>();


	private Member member;

	@Enumerated(EnumType.STRING)
	private DiscordAuth discodeAuth; //인증 여부

}

