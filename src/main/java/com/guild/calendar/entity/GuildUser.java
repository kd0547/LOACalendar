package com.guild.calendar.entity;

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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.guild.calendar.constant.LoaClass;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
@Entity

public class GuildUser extends BaseEntity {
	
	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "GuildUser_SEQ_GENERATOR")
	@Column(name = "guild_user_id")
	private Long id;
	
	@JoinColumn(name = "guild_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Guild guild;
	
	
	private String GuildName;
	
	@Enumerated(EnumType.STRING)
	private LoaClass loaClass; 
	
	private int level;
	
	
	@Column(unique = true)
	private String username;

	
}
