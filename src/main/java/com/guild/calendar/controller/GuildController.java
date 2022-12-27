package com.guild.calendar.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.guild.calendar.dto.ErrorCode;
import com.guild.calendar.dto.GuildForm;
import com.guild.calendar.service.GuildService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/guild")
@RequiredArgsConstructor
public class GuildController {
	
	private final GuildService guildService;
	
	@GetMapping("/view")
	public ResponseEntity<List<GuildForm>> viewGuild(Principal principal) {
		String username = principal.getName();
		
		List<GuildForm> findGuild = guildService.findAllGuild(username);
		
		
		return ResponseEntity.ok(findGuild);
	}
	
	@PostMapping("/create")
	public @ResponseBody Object createGuild(Principal principal,@RequestBody GuildForm guildForm) {
		String username = principal.getName();
		guildForm.setGuildOwner(username);
		
		guildService.saveGuild(guildForm);
		
		
		
		return null;
		
	}
	@PostMapping("/update/{id}")
	public @ResponseBody Object updateGuild(Principal principal,@RequestBody GuildForm guildForm) {
		String username = principal.getName();
		guildForm.setGuildOwner(username);
		
		guildService.updateGuild(guildForm);
		
		

		return null;
		
	}
	@PostMapping("/delete/{id}")
	public @ResponseBody Object deleteGuild(Principal principal,@RequestBody GuildForm guildForm) {
		String username = principal.getName();
		guildForm.setGuildOwner(username);
		
		guildService.deleteGuild(guildForm);
		
		

		return null;
		
	}
	
	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseEntity<ErrorCode> handleDuplicateKeyException(DuplicateKeyException exception) {
		//https://mangkyu.tistory.com/204
		//https://tecoble.techcourse.co.kr/post/2020-08-31-http-status-code/
		ErrorCode errorCode = new  ErrorCode(HttpStatus.BAD_REQUEST,exception.getMessage());
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorCode);
	}
	
}
