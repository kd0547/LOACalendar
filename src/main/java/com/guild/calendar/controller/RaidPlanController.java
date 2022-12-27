package com.guild.calendar.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.guild.calendar.dto.RaidPlanRequestDto;
import com.guild.calendar.service.RaidPlanGuildUserService;
import com.guild.calendar.service.RaidPlanService;
import com.guild.calendar.sub.RequestTime;

@Controller
@RequestMapping("/plan")
public class RaidPlanController {
	
	private final RaidPlanService raidPlanService;
	private final RaidPlanGuildUserService raidPlanGuildUserService;
	private final RequestTime requestTime;
	
	
	@Autowired
	public RaidPlanController(RaidPlanService raidPlanService,RaidPlanGuildUserService raidPlanGuildUserService,RequestTime requestTime) {
		this.raidPlanService = raidPlanService;
		this.raidPlanGuildUserService = raidPlanGuildUserService;
		this.requestTime = requestTime;
	}
	
	@ResponseBody
	@PostMapping("/create")
	public void createRaidPlan(Principal principal,@RequestBody RaidPlanRequestDto planRequestDto) {
		requestTime.logTest("createRaidPlan 메서드 실행");
		
		
		raidPlanService.createRaidPlan(planRequestDto);
		
	}
}
