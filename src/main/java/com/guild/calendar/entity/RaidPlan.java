package com.guild.calendar.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
import javax.persistence.OneToMany;

import com.guild.calendar.constant.LegionRaid;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
public class RaidPlan extends BaseEntity{
	@Id @GeneratedValue(strategy = GenerationType.AUTO,generator = "RaidPlan_SEQ_GENERATOR")
	@Column(name = "raid_plan")
	private Long id;
	
	
	@Enumerated(EnumType.STRING)
	private LegionRaid legionRaid;
	
	private LocalDateTime raidStartDateTime;

	
}
