package com.guild.calendar.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.guild.calendar.dto.ErrorCode;
import com.guild.calendar.dto.MemberForm;
import com.guild.calendar.dto.SuccessCode;
import com.guild.calendar.service.CalendarService;
import com.guild.calendar.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
	
	
	private final MemberService memberService;
	
	
	@PostMapping("/signup")
	public ResponseEntity<?> signup(@RequestBody MemberForm memberForm) {
		Long saveId = memberService.saveMember(memberForm);
		
		SuccessCode successCode = new SuccessCode();
		
		successCode.setStateCode(HttpStatus.OK.value());
		successCode.setId(saveId);
		successCode.setMessage("회원가입 완료");
		
		
		return ResponseEntity.status(HttpStatus.OK).body(successCode);
	}
	
	
	
	
	
	
	@PutMapping("/update")
	public ResponseEntity<?> updateMember(Principal principal) {
		
		return null;
	}
	
	
	
	
	
	@PostMapping("/delete")
	public ResponseEntity<?> deletMember() {
		
		return null;
	}
	
	
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorCode> handleIllegalStateException(IllegalStateException exception) {
		//https://mangkyu.tistory.com/204
		//https://tecoble.techcourse.co.kr/post/2020-08-31-http-status-code/
		ErrorCode errorCode = new  ErrorCode(HttpStatus.BAD_REQUEST, exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCode);
	}
}
