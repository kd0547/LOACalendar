package com.guild.calendar.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guild.calendar.Exception.NotFoundException;
import com.guild.calendar.dto.CalendarDto;
import com.guild.calendar.dto.CalendarShareURL;
import com.guild.calendar.dto.CalendarShortInfoDto;
import com.guild.calendar.dto.ClendarUpdateForm;
import com.guild.calendar.dto.ErrorCode;
import com.guild.calendar.dto.ShareURL;
import com.guild.calendar.dto.SuccessCode;
import com.guild.calendar.repository.MemberRepository;
import com.guild.calendar.security.DesEncryption;
import com.guild.calendar.service.CalendarService;
import com.guild.calendar.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {
	
	
	private final CalendarService calendarService;
	
	private final DesEncryption desEncryption;
	
	private  ObjectMapper objectMapper;
	
	private final MemberService memberService;
	
	@PostConstruct
	public void init() {
		objectMapper = new ObjectMapper();
	}
	
	
	


	@GetMapping
	public ResponseEntity<?> viewCalendar(Principal principal) {
		String username = principal.getName();
		
		List<CalendarDto> calendarDtos = calendarService.findByCalendar(username);
		
		
		return ResponseEntity.status(HttpStatus.OK).body(calendarDtos);
	}
	
	@GetMapping("/share/url/{yearMonth}")
	public ResponseEntity<?> shreURLCreate(@RequestParam("encode") String encode,@PathVariable("yearMonth")int yearMonth) {
		if(encode != null) {
			String username = desEncryption.decrypttURL(encode);
			
			System.out.println(username);
		}
		
		
		
		
		return null;
	}
	
	
	@PostMapping("/share/{id}")
	public ResponseEntity<?> shreURLCreate(Principal principal,@PathVariable("id") Long id) {
		String username = principal.getName();
		
		Long memberId = memberService.findByEmail(username);
		
		CalendarShareURL calendarShareURL = new CalendarShareURL();
		calendarShareURL.setCalendarId(id);
		calendarShareURL.setMemberId(memberId);
		
		try {
			ShareURL shareURL = new ShareURL();
			String rawData = objectMapper.writeValueAsString(calendarShareURL);
			String encode = desEncryption.encryptURL(rawData);
			
			shareURL.setUrl(encode);
			shareURL.setExpiredTime(30);
			
			return ResponseEntity.status(HttpStatus.CREATED).body(shareURL);
			
		} catch (JsonProcessingException e) {
			
			e.printStackTrace();
			
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorCode(HttpStatus.INTERNAL_SERVER_ERROR,"서버 장애 발생"));
		}
	}
	
	
	@PostMapping
	public ResponseEntity<?> createCalendar(Principal principal,@RequestBody CalendarDto calendarDto) {
		String username = principal.getName();
		calendarDto.setUsername(username);
		
		
		Long saveId = calendarService.createCalendar(calendarDto);
		 
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessCode(HttpStatus.CREATED.value(), saveId, "생성 성공"));
	}
	
	
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCalendar(Principal principal,@RequestBody ClendarUpdateForm clendarUpdateForm) {
		String username = principal.getName();
		
		try {
			calendarService.updateCalendar(username,clendarUpdateForm);
			
			return ResponseEntity.status(HttpStatus.OK).body(new SuccessCode(HttpStatus.OK.value(),clendarUpdateForm.getId(),"수정 성공"));
		} catch (NullPointerException e) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("test");
		}
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCalendar(Principal principal,@PathVariable Long id) {
		calendarService.deleteCalendar(principal.getName(),id);
		
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
}
