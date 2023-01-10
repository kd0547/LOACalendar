package com.guild.calendar.controller;

import java.security.Principal;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.guild.calendar.dto.ErrorCode;
import com.guild.calendar.dto.GuildForm;
import com.guild.calendar.dto.GuildUserDto;
import com.guild.calendar.dto.SuccessCode;
import com.guild.calendar.service.GuildService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/guild")
@RequiredArgsConstructor
public class GuildController {
	
	private final GuildService guildService;
	//
	@GetMapping("/view")
	public ResponseEntity<?> viewGuild(Principal principal) {
		String username = principal.getName();
		
		List<GuildForm> findGuild = guildService.findAllGuild(username);
		
		System.out.println("test");
		
		return ResponseEntity.status(HttpStatus.OK).body(findGuild);
	}
	
	
	
	
	@PostMapping
	public ResponseEntity<?> createGuild(Principal principal,@RequestBody GuildForm guildForm) {
		String username = principal.getName();
		guildForm.setGuildOwner(username);
		
		Long saveId= guildService.saveGuild(guildForm);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessCode(HttpStatus.CREATED.value(), saveId, "길드 생성 성공"));
	}
	
	
	@PutMapping("/{guildId}")
	public  ResponseEntity<?> updateGuild(Principal principal,@RequestBody GuildForm guildForm) {
		String username = principal.getName();
		
		guildService.updateGuild(username,guildForm);
		
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body("");
	}
	
	
	@DeleteMapping("/{guildId}")
	public  ResponseEntity<?> deleteGuild(Principal principal,@PathVariable Long guildId) {
		String username = principal.getName();
			
		guildService.deleteGuild(username,guildId);
		

		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SuccessCode(HttpStatus.ACCEPTED.value(),guildId,"삭제 완료"));

	}
	/**
	 * 길드 유저를 조회합니다.
	 * 
	 * @param principal
	 * @param guildId
	 * @return
	 */
	
	// /guild/{1}/guild-users
	@GetMapping("/{guildId}/guild-users")
	public ResponseEntity<?> viewGuildUser (Principal principal,@PathVariable Long guildId) {
		String username = principal.getName();
			
		List<GuildUserDto> guildUserDtos = guildService.findAllGuildUser(username,guildId);
			
			
		return ResponseEntity.status(HttpStatus.OK).body(guildUserDtos);
	}
	
	
	
	/**
	 * 
	 * @param principal
	 * @return
	 */
	//길드 유저 생성
	@PostMapping("/{guildId}/guild-user")
	public ResponseEntity<?> createGuildUser(Principal principal,@PathVariable Long guildId,@RequestBody GuildUserDto guildUserDto) {
		String username = principal.getName();
		Long saveId = guildService.saveGuildUser(username,guildId,guildUserDto);
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessCode(HttpStatus.CREATED.value(), saveId, "유저 생성 완료"));
	}
	
	
	
	// /guild/{1}/guild-user/{10}
	@PutMapping("/{guildId}/guild-user/{guildUserId}")
	public ResponseEntity<?> updateGuildUser(Principal principal,@PathVariable Long guildId,@PathVariable Long guildUserId,@RequestBody GuildUserDto guildUserDto) {
		String username = principal.getName();
		
		System.out.println(guildUserDto.toString());
		
		Long updateId = guildService.updateGuildUser(username,guildId,guildUserId,guildUserDto);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SuccessCode(HttpStatus.ACCEPTED.value(),updateId,"변경 완료"));
	}
	
	
	
	
	@DeleteMapping("/{guildId}/guild-user/{guildUserId}")
	public ResponseEntity<?> deleteGuildUser(Principal principal,@PathVariable Long guildId,@PathVariable Long guildUserId) {
		String username = principal.getName();
		
		guildService.deleteGuildUser(username,guildId,guildUserId);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SuccessCode(HttpStatus.ACCEPTED.value(),null,"삭제 완료"));
	}
	
	
	
	
	
	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<ErrorCode> handleDuplicateKeyException(DuplicateKeyException exception) {
		//https://mangkyu.tistory.com/204
		//https://tecoble.techcourse.co.kr/post/2020-08-31-http-status-code/
		ErrorCode errorCode = new  ErrorCode(HttpStatus.BAD_REQUEST,exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCode);
	}
	
	@ExceptionHandler(NullPointerException.class)
	public ResponseEntity<ErrorCode> handleNullponterException(NullPointerException exception) {
		ErrorCode errorCode = new  ErrorCode(HttpStatus.BAD_REQUEST,exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCode);
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorCode> handleIllegalArgumentException(IllegalArgumentException exception) {
		ErrorCode errorCode = new  ErrorCode(HttpStatus.BAD_REQUEST,exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCode);
	}
	
	
}
