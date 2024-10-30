package com.guild.calendar.entity;

import jakarta.persistence.*;

import com.guild.calendar.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @ToString
public class Calendar extends BaseEntity {

	@Column(name = "calendar_id")
	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "Calendar_SEQ_GENERATOR")
	private Long id;

	@Column(nullable = false)
	private String subject;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member; //소유자 정보

	@OneToMany(mappedBy = "calendar",fetch = FetchType.LAZY)
	private List<RaidPlan> raidPlans = new ArrayList<>();
}

