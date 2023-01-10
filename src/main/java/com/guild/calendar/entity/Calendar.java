package com.guild.calendar.entity;


import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@Entity
public class Calendar extends BaseEntity {
	
	@Column(name = "calendar_id")
	@Id @GeneratedValue(strategy = GenerationType.AUTO, generator = "Calendar_SEQ_GENERATOR")
	private Long id;
	
	@Column(nullable = false)
	private String subject;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id")
	private Member member;
	
	
	private String shareUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="guild_id")
	private Guild guild;
	
	//해당 데이터의 생성자 또는 소유자 ID
	private String owner;
	
	public Calendar() {}
	public Calendar(Builder builder) {
		this.subject = builder.subject;
		this.shareUser = builder.shareUser;
		this.member = builder.member;
		this.guild = builder.guild;
	}
	
	public static class Builder {
		private String subject;
		private Member member;
		private String shareUser;
		private Guild guild;
		
		public Builder subject(String subject) {this.subject = subject; return this;}
		public Builder member(Member member) {this.member = member; return this;}
		public Builder shareUser(String shareUser) {this.shareUser = shareUser; return this;}
		
		/**
		 * 인증 완료 후 길드
		 * @param guildName 
		 * @return
		 */
		public Builder guild(Guild guild) {this.guild = guild; return this;}
		public Calendar build() {return new Calendar(this);};
	}

	
	

}
