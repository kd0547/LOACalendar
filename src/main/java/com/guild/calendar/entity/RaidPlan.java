package com.guild.calendar.entity;



import java.time.LocalDate;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


import com.guild.calendar.constant.LegionRaid;
import com.guild.calendar.dto.RaidPlanRequestDto;

import com.guild.calendar.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@ToString
@Getter
@Setter
public class RaidPlan extends BaseEntity {
	@Id @GeneratedValue(strategy = GenerationType.AUTO,generator = "RaidPlan_SEQ_GENERATOR")
	@Column(name = "raid_plan")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "calendar_id")
	private Calendar calendar;
	
	@Enumerated(EnumType.STRING)
	private LegionRaid legionRaid;
	
	private LocalDate raidStartDate;
	
	private LocalTime raidStartTime;
	
	//해당 데이터의 생성자 또는 소유자 ID
	private String owner;
	
	public void patchUpdateRaidPlan(RaidPlanRequestDto planRequestDto) {
		
		if(planRequestDto.getRaid() != null) {
			this.legionRaid = planRequestDto.getRaid();
		}
		
		if(planRequestDto.getStartDate() != null) {
			this.raidStartDate = planRequestDto.getStartDate();
		}
		
		if(planRequestDto.getStartTime() != null) {
			this.raidStartTime = planRequestDto.getStartTime();
		}
		
	}
	
}
