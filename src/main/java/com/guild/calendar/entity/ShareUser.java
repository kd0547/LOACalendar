package com.guild.calendar.entity;

import com.guild.calendar.entity.base.BaseEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ShareUser extends BaseEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
}
