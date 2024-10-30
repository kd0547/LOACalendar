package com.guild.calendar.entity;


import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.guild.calendar.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/*
@TableGenerator(
		name = "RaidPlanGuildUser_SEQ_GENERATOR",
		table = "raid_plan_guild_user_seq_table",
		pkColumnName = "RaidPlanGuildUser_SEQ",
		valueColumnName = "next_val",
		initialValue = 1,
		allocationSize = 20
		)
*/
@Entity @Getter @Setter @ToString
public class CalendarDetail extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "calendar_detail_GENERATOR")
	@Column(name = "calendar_detail_id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "calendar_id")
	private Calendar calendar;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "raid_plan")
	private RaidPlan raidPlan;
	
	private LocalDate startDate;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "guild_user_id")
	private GuildUser guildUser;

	
	
}
