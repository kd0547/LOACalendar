package com.guild.calendar.entity;



import java.time.LocalDate;
import java.time.LocalTime;

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


import com.guild.calendar.constant.LegionRaid;

import com.guild.calendar.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString @Getter @Setter
public class RaidPlan extends BaseEntity {
	@Id @GeneratedValue(strategy = GenerationType.AUTO,generator = "RaidPlan_SEQ_GENERATOR")
	@Column(name = "raid_plan")
	private Long id;


	private String subject; //제목

	private Calendar calendar;
	
	@Enumerated(EnumType.STRING)
	private LegionRaid legionRaid;
	
	private LocalDate raidStartDate; //레이드 시작 날짜

	private LocalTime raidStartTime; //레이드 시작 시간
}
