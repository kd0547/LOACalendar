package com.guild.calendar.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guild.calendar.dto.CalendarDto;
import com.guild.calendar.dto.CalendarShortInfoDto;
import com.guild.calendar.service.CalendarService;

@Controller
@RequestMapping("/calendar")
public class CalendarController {
	private final CalendarService calendarService;
	
	@Autowired
	public CalendarController(CalendarService calendarService) {
		this.calendarService = calendarService;
	}
	
	
	@ResponseBody
	@GetMapping("list")
	public List<CalendarShortInfoDto> viewCalendar(Principal principal) {
		String username = principal.getName();
		
		List<CalendarShortInfoDto> calendarShortInfoDto = calendarService.viewCalendar(username);
		
		if(calendarShortInfoDto.size() == 0) {
			return null;
		}
		
		
		return calendarShortInfoDto;
	}
	
	
	@ResponseBody
	@PostMapping("/view")
	public String viewCalendar() {
		//@RequestParam("year")int year, @RequestParam("month")int month
		//LocalDateTime startDate = LocalDateTime.of(year, month, 0, 0, 0, 0);
			
		//calendarService.calendarYearMonth("죄송군단장",startDate);
		
		System.out.println("test");
		
		
		return "test";
	}
	
	
	@ResponseBody
	@PostMapping("/create")
	public String createCalendar(Principal principal,@RequestBody CalendarDto calendarDto) {
		String username = principal.getName();
		
		calendarDto.setUsername(username);
		
		calendarService.createCalendar(calendarDto);
		
		
		return "test";
	}
	
	@ResponseBody
	@PostMapping("/update/{id}")
	public String updateCalendar(Principal principal,@RequestBody CalendarDto calendarDto) {
		String username = principal.getName();
		calendarDto.setUsername(username);
		
		System.out.println(calendarDto.toString());
		
		calendarService.updateCalendar(calendarDto);
		
		return "test";
	}
	
	@ResponseBody
	@GetMapping("/delete")
	public String deleteCalendar() {
		
		return "test";
	}
}
