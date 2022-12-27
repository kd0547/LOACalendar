package com.guild.calendar.dto;

import java.time.LocalDate;


import com.guild.calendar.entity.Calendar;
import com.guild.calendar.entity.Guild;
import com.guild.calendar.entity.Member;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class CalendarShortInfoDto {
	private Long id;
	
	private String subject;
	
	private String memberId; //member_id;
	
	private LocalDate createDate;
	
	
	/**
	 * 
	 * @param calendar
	 * @return CalendarShortInfoDto
	 */
	public static CalendarShortInfoDto createCalendarShortInfoDto(Calendar calendar) {
		
		CalendarShortInfoDto calendarShortInfoDto = 
					new CalendarShortInfoDto.Builder()
						.id(calendar.getId())
						.subject(calendar.getSubject())
						.member(calendar.getMember())
						.createDate(calendar.getRegTime().toLocalDate())
						.build();
		
		
		return calendarShortInfoDto;
	}
	
	
	
	
	
	public CalendarShortInfoDto() {}
	public CalendarShortInfoDto(Builder builder) {
		this.id = builder.id;
		this.subject = builder.subject;
		this.memberId = builder.memberId;
		this.createDate = builder.createDate;
		
	}
	
	public static class Builder {
		private Long id;
		private String subject;
		private String memberId; //member_id;
		private LocalDate createDate;
		
		
		public Builder id(Long id) {this.id = id; return this;}
		public Builder member(Member member) {
			// TODO Auto-generated method stub
			return null;
		}
		public Builder subject(String subject) {this.subject = subject; return this;}
		public Builder memberId(String memberId) {this.memberId = memberId; return this;}	
		public Builder createDate(LocalDate createDate) {this.createDate = createDate; return this;}
		
		
		public CalendarShortInfoDto build() {return new CalendarShortInfoDto(this);};
		

	}
}
