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

import com.guild.calendar.Exception.ExceptionCode;
import com.guild.calendar.dto.ErrorCode;
import com.guild.calendar.dto.GuildForm;
import com.guild.calendar.dto.GuildUserDto;
import com.guild.calendar.dto.ResponseGuildUserDto;
import com.guild.calendar.dto.SuccessCode;
import com.guild.calendar.service.GuildService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/guild")
@RequiredArgsConstructor
public class GuildController {
	
	private final GuildService guildService;
	
	
	/**
	 *	
	 * @param principal
	 * @return
	 */
	@GetMapping
	public ResponseEntity<?> viewGuild(Principal principal) {
		String username = principal.getName();
		
		List<GuildForm> findGuild = guildService.findAllGuild(username);
		
	
		
		return ResponseEntity.status(HttpStatus.OK).body(findGuild);
	}
	
	
	
	/**
	 * 길드를 생성합니다. 
	 * @param principal
	 * @param guildForm
	 * @return
	 */
	@PostMapping
	public ResponseEntity<?> createGuild(Principal principal,@RequestBody GuildForm guildForm) {
		String username = principal.getName();
		guildForm.setGuildOwner(username);
		
		Long saveId= guildService.saveGuild(guildForm);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessCode(HttpStatus.CREATED.value(), saveId));
	}
	
	/**
	 * 
	 * 길드를 수정합니다. 
	 * 
	 * @param principal
	 * @param guildId
	 * @param guildForm
	 * @return
	 */
	@PutMapping("/{guildId}")
	public  ResponseEntity<?> updateGuild(Principal principal,@PathVariable Long guildId,@RequestBody GuildForm guildForm) {
		String username = principal.getName();
		
		guildService.updateGuild(username,guildId,guildForm);
		
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SuccessCode(HttpStatus.ACCEPTED.value(),guildId));
	}
	
	/**
	 * 길드를 삭제합니다. 길드 유저가 존재할 경우 삭제할 수 없습니다. 
	 * @param principal
	 * @param guildId
	 * @return
	 */
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
			
		ResponseGuildUserDto responseGuildUserDto = guildService.findAllGuildUser(username,guildId);
			
			
		return ResponseEntity.status(HttpStatus.OK).body(responseGuildUserDto);
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
		
		
		return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessCode(HttpStatus.CREATED.value(), saveId));
	}
	
	
	/**
	 * 길드 유저의 정보를 변경합니다. 
	 * 
	 * @param principal
	 * @param guildId
	 * @param guildUserId
	 * @param guildUserDto
	 * @return
	 */
	// /guild/{1}/guild-user/{10}
	@PutMapping("/{guildId}/guild-user/{guildUserId}")
	public ResponseEntity<?> updateGuildUser(Principal principal,@PathVariable Long guildId,@PathVariable Long guildUserId,@RequestBody GuildUserDto guildUserDto) {
		String username = principal.getName();
		/*
		 * @RequestBody에서 데이터를 바인딩할 때 Enum 타입인 LoaClass의 처리 방법을 생각해보기 
		 *	 
		 * 
		 */
		System.out.println(guildUserDto.toString());
		
		Long updateId = guildService.updateGuildUser(username,guildId,guildUserId,guildUserDto);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SuccessCode(HttpStatus.ACCEPTED.value(),updateId));
	}
	
	
	
	/**
	 * 길드 유저를 삭제합니다. 
	 * @param principal
	 * @param guildId
	 * @param guildUserId
	 * @return
	 */
	@DeleteMapping("/{guildId}/guild-user/{guildUserId}")
	public ResponseEntity<?> deleteGuildUser(Principal principal,@PathVariable Long guildId,@PathVariable Long guildUserId) {
		String username = principal.getName();
		
		Long deleteGuildUser = guildService.deleteGuildUser(username,guildId,guildUserId);
		
		return ResponseEntity.status(HttpStatus.ACCEPTED).body(new SuccessCode(HttpStatus.ACCEPTED.value(),deleteGuildUser));
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
		String errorMessage = exception.getMessage();
		
		
		
		return null;
	}
	
	@ExceptionHandler(IllegalStateException.class)
	public ResponseEntity<ErrorCode> handleIllegalStateException(IllegalStateException exception) {
		String errorMessage = exception.getMessage();
		
		if(errorMessage.equals("-150")) {
			String code = ExceptionCode.NO_SUCH_GUILD.getCode();
			String message = ExceptionCode.NO_SUCH_GUILD.getMessage();
		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCode(HttpStatus.BAD_REQUEST, code, message));
		}
		
		if(errorMessage.equals("-151")) {
			String code = ExceptionCode.DUPLICATE_GUILDUUSER.getCode();
			String message = ExceptionCode.DUPLICATE_GUILDUUSER.getMessage();
		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCode(HttpStatus.BAD_REQUEST, code, message));
		}
		
		
		if(errorMessage.equals("-152")) {
			String code = ExceptionCode.USED_GUILD.getCode();
			String message = ExceptionCode.USED_GUILD.getMessage();
		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCode(HttpStatus.BAD_REQUEST, code, message));
		}
		
		
		return null;
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorCode> handleIllegalArgumentException(IllegalArgumentException exception) {
		String errorMessage = exception.getMessage();
		
		if(errorMessage.equals("-150")) {
			String code = ExceptionCode.NO_SUCH_GUILD.getCode();
			String message = ExceptionCode.NO_SUCH_GUILD.getMessage();
		
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorCode(HttpStatus.BAD_REQUEST, code, message));
		}
		
		
		return null;
	}
	
	
	
}
