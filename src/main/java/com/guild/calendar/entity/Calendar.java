package com.guild.calendar.entity;

import com.guild.calendar.dto.CalendarsDto;
import jakarta.persistence.*;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter @ToString
public class Calendar {

	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String subject;

	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id")
	private Users users;

	@OneToMany(mappedBy = "calendar",fetch = FetchType.LAZY)
	private List<RaidPlan> raidPlans = new ArrayList<>();

	public static Calendar createCalendar(Long userId, CalendarsDto calendarsDto) {
		Calendar calendar = new Calendar();
		calendar.setSubject(calendarsDto.getSubject());
		calendar.setDescription(calendarsDto.getDescription());
		calendar.setUsers(new Users(userId));

		return calendar;
	}
}

