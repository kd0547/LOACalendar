package com.guild.calendar.entity;



import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;


import com.guild.calendar.constant.LegionRaid;

import com.guild.calendar.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString @Getter @Setter
public class RaidPlan extends BaseEntity {
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "raid_plan_id")
	private Long id;

	private String subject; //제목

	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "calendar_id")
	private Calendar calendar;

	@OneToMany(mappedBy = "raidPlan", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<RaidPlanDetail> raidPlanDetails = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	private LegionRaid legionRaid;
	
	private LocalDate raidStartDate; //레이드 시작 날짜

	private LocalTime raidStartTime; //레이드 시작 시간
}
