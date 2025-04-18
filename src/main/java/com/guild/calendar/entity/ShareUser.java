package com.guild.calendar.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ShareUser {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
}
