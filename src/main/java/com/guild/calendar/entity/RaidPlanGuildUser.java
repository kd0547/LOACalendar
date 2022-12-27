package com.guild.calendar.entity;


import java.time.LocalDateTime;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.TableGenerator;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
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
public class RaidPlanGuildUser extends BaseEntity{

	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "raid_plan_guild_user_seq_GENERATOR")
	@Column(name = "raid_plan_guild_user_id")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "calendar_id")
	private Calendar calendar;
	
	@ManyToOne
	@JoinColumn(name = "raid_plan")
	private RaidPlan raidPlan;
	
	
	
	@ManyToOne
	@JoinColumn(name = "guild_user_id")
	private GuildUser guildUser;

	
	
	private LocalDateTime raidStartDateTime;
	
	
}
