package com.guild.calendar.entity;

import com.guild.calendar.entity.base.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class ShareUser extends BaseEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO,generator = "")
	private Long id;
}
