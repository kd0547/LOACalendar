package com.guild.calendar.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter @Setter @ToString
public class RaidPlanDetail {

	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "calendar_detail_GENERATOR")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "raid_plan_id")
	private RaidPlan raidPlan;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guild_user_id")
	private GuildUser guildUser;

}
