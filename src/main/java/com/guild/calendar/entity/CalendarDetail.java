package com.guild.calendar.entity;


import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

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
@Entity
@Getter @Setter @ToString
public class CalendarDetail extends BaseEntity {

	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "calendar_detail_GENERATOR")
	@Column(name = "calendar_detail_id")
	private Long id;
	
	private Calendar calendar;

	private RaidPlan raidPlan;

	private GuildUser guildUser;

}
