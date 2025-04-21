package com.guild.calendar.controller;

import com.guild.calendar.dto.RaidPlanDto;
import com.guild.calendar.jwt.CustomUserDetails;
import com.guild.calendar.service.RaidPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/raid")
public class RaidPlanController {

    private final RaidPlanService raidPlanService;

    @GetMapping("/{calendarId}")
    public ResponseEntity<?> viewRaidPlan(Authentication authentication, @PathVariable Long calendarId) {
        Long userId = ((CustomUserDetails)authentication.getPrincipal()).getId();
        RaidPlanDto raidPlanDto = raidPlanService.viewRaidPlan(userId,calendarId);
        return ResponseEntity.ok("");
    }
}
