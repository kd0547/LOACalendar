package com.guild.calendar.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guild.calendar.Exception.ExceptionCode;
import com.guild.calendar.Exception.NotFoundException;
import com.guild.calendar.dto.CalendarDetailsDto;
import com.guild.calendar.dto.CalendarDto;
import com.guild.calendar.dto.CalendarShareURL;
import com.guild.calendar.dto.CalendarShortInfoDto;
import com.guild.calendar.dto.ClendarUpdateForm;
import com.guild.calendar.dto.ErrorCode;
import com.guild.calendar.dto.ShareURL;
import com.guild.calendar.dto.SuccessCode;
import com.guild.calendar.entity.CalendarDetail;
import com.guild.calendar.repository.MemberRepository;
import com.guild.calendar.security.DesEncryption;
import com.guild.calendar.service.CalendarDetailService;
import com.guild.calendar.service.CalendarService;
import com.guild.calendar.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {
	
	
	private final CalendarService calendarService;
	
	private final DesEncryption desEncryption;
	
	private final ObjectMapper objectMapper;
	
	private final MemberService memberService;
	
	
	private final CalendarDetailService calendarDetailService;
	
	


	@GetMapping
	public ResponseEntity<?> viewCalendar(Principal principal) {
		String username = principal.getName();
		
		List<CalendarDto> calendarDtos = calendarService.findByCalendar(username);
		
		
		return ResponseEntity.status(HttpStatus.OK).body(calendarDtos);
	}
	
	@GetMapping("/share/url/{yearMonth}")
	public ResponseEntity<?> shreURLCreate(@RequestParam("encode") String encode,@PathVariable("yearMonth")String yearMonth) {
		if(encode != null) {
			System.out.println(encode);
			
			String object = desEncryption.decrypttURL(encode);
			
			
			try {
				CalendarShareURL calendarShareURL = objectMapper.readValue(object, CalendarShareURL.class);
				LocalDate date = calendarShareURL.getExpiredTime();
				
				String paddingYearMonth = yearMonth + "01";
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.KOREA);
				
				LocalDate requestDate = LocalDate.parse(paddingYearMonth, formatter);
				
				//System.out.println(requestDate);
				
				LocalDate nowDate = LocalDate.now();
				
				
				if(nowDate.isBefore(date)) {
					
					CalendarDetailsDto calendarDetailsDto = calendarDetailService.findCalendarAll(requestDate,calendarShareURL.getCalendarId(),calendarShareURL.getMemberId());
					calendarDetailsDto.setYearMonth(yearMonth);
					
					
					return ResponseEntity.status(HttpStatus.OK).body(calendarDetailsDto);
				}
				
				//calendarService.findByCalendarId(null)
				
				
			} catch (JsonProcessingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		
		
		
		return null;
	}
	public void isDateBefore(LocalDate nowDate, LocalDate otherDate) {
		
	}
	
	
	@PostMapping("/share/{id}")
	public ResponseEntity<?> shreURLCreate(Principal principal,@PathVariable("id") Long id) {
		String username = principal.getName();
		
		Long memberId = memberService.findByEmail(username);
		
		CalendarShareURL calendarShareURL = new CalendarShareURL();
		calendarShareURL.setCalendarId(id);
		calendarShareURL.setMemberId(memberId);
		calendarShareURL.setExpiredTime(LocalDate.now().plusDays(30L));
		try {
			ShareURL shareURL = new ShareURL();
			String rawData = objectMapper.writeValueAsString(calendarShareURL);
			String encode = desEncryption.encryptURL(rawData);
			
			shareURL.setUrl(encode);
			
			
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
	
	
	/**
	 * 캘린더의 ID와 수정할 제목을 받아 실제 보유한 데이터인지 확인한 후 데이터를 수정합니다. 
	 * 
	 * @param principal
	 * @param id
	 * @param clendarUpdateForm
	 * @return
	 */
	@PutMapping("/{id}")
	public ResponseEntity<?> updateCalendar(Principal principal,@PathVariable Long id ,@RequestBody ClendarUpdateForm clendarUpdateForm) {
		String username = principal.getName();
		
		calendarService.updateCalendar(username,id,clendarUpdateForm);
		
		return ResponseEntity.status(HttpStatus.OK).body(new SuccessCode(HttpStatus.OK.value(),id));
	}
	
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteCalendar(Principal principal,@PathVariable Long id) {
		calendarService.deleteCalendar(principal.getName(),id);
		
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(null);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorCode> handleNullPointerException(NullPointerException exception) {
		String errorMessage = exception.getMessage();
		
		if(errorMessage.equals("-190")) {
			String code = ExceptionCode.NO_SUCH_CALENDAR.getCode();
			String message = ExceptionCode.NO_SUCH_CALENDAR.getMessage();
		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCode(HttpStatus.BAD_REQUEST, code, message));
		}
		return null;	
	}
	
}
