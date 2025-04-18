package com.guild.calendar.entity;

import jakarta.persistence.*;

import com.guild.calendar.constant.DiscordAuth;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@ToString @Getter @Setter
public class Guild {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	private String guildName;

	private String guildDescription;

	@OneToMany(mappedBy = "guild")
	private List<GuildUser> guildUsers = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id")
	private Users users; //소유자

	@Enumerated(EnumType.STRING)
	private DiscordAuth discodeAuth; //인증 여부

}

