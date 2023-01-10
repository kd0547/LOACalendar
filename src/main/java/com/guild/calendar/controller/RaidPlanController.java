package com.guild.calendar.controller;

import java.security.Principal;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.guild.calendar.dto.ErrorCode;
import com.guild.calendar.dto.RaidPlanRequestDto;
import com.guild.calendar.dto.RaidPlanResponseDto;
import com.guild.calendar.service.CalendarDetailService;
import com.guild.calendar.sub.RequestTime;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/plan")
@RequiredArgsConstructor
public class RaidPlanController {
	
	
	private final CalendarDetailService calendarDetailService;
	private final RequestTime requestTime;
	
	
	
	
	/**
	 * 
	 * @param principal
	 * @param planId
	 * @param calendarId
	 * @return
	 */
	@GetMapping("/{planId}/calendar/{calendarId}")
	public ResponseEntity<?> viewRaidPlan(Principal principal,@PathVariable Long planId,@PathVariable Long calendarId) {
		String username = principal.getName();
		
		RaidPlanResponseDto raidPlanResponseDto = calendarDetailService.findRaidPlan(username,planId,calendarId);

		return ResponseEntity.status(HttpStatus.OK).body(raidPlanResponseDto);
	}
	
	
	@PostMapping("/calendar/{calendarId}")
	public ResponseEntity<?> createRaidPlan(Principal principal,@PathVariable Long calendarId, @RequestBody RaidPlanRequestDto planRequestDto) {
		requestTime.logTest("createRaidPlan 메서드 실행");
		
		String username = principal.getName();
		
		calendarDetailService.createRaidPlan(username,calendarId,planRequestDto);
		return ResponseEntity.status(HttpStatus.CREATED).body("요청 성공");
	}
	
	
	
	
	
	@PutMapping("/{planId}/calendar/{calendarId}")
	public ResponseEntity<?> putUpdateRaidPlan(Principal principal,@RequestBody RaidPlanRequestDto planRequestDto) {
		
		calendarDetailService.updateRaidPlanGuildUser(planRequestDto);
		
		return ResponseEntity.status(HttpStatus.OK).body("");
	}
	
	@PatchMapping("/{planId}/calendar/{calendarId}")
	public ResponseEntity<?> patchUpdateRaidPlan(Principal principal,@PathVariable Long planId,@PathVariable Long calendarId,@RequestBody RaidPlanRequestDto planRequestDto) {
		String username = principal.getName();
		
		calendarDetailService.updateRaidPlanGuildUser(username,planId,calendarId,planRequestDto);
		
		
		return ResponseEntity.status(HttpStatus.OK).body("");
	}
	
	
	@DeleteMapping("/{planId}/calendar/{calendarId}")
	public ResponseEntity<?> deleteRaidPlan(Principal principal,@PathVariable Long planId,@PathVariable Long calendarId) {
		String username = principal.getName();
		
		calendarDetailService.deleteRaidPlan(username,planId,calendarId);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body("");
	}
	
	
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorCode> handleIllegalArgumentException(IllegalArgumentException exception) {
		ErrorCode errorCode = new  ErrorCode(HttpStatus.BAD_REQUEST,exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCode);
	}
	
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorCode> handleNullponterException(NullPointerException exception) {
		ErrorCode errorCode = new  ErrorCode(HttpStatus.BAD_REQUEST,exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCode);
	}
	
	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorCode> handleNoSuchElementException(NoSuchElementException exception) {
		ErrorCode errorCode = new  ErrorCode(HttpStatus.BAD_REQUEST,"잘못된 접근입니다.");
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCode);
	}
	
	
}
