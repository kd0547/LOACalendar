package com.guild.calendar.controller;

import com.guild.calendar.dto.CalendarsDto;
import com.guild.calendar.jwt.CustomUserDetails;
import com.guild.calendar.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/calendar")
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/view")
    public ResponseEntity<?> viewCalendar(Authentication authentication) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getId();
        List<CalendarsDto> calendarsDtos = calendarService.viewCalendars(userId);

        return ResponseEntity.ok(calendarsDtos);
    }


    @PostMapping("/create")
    public ResponseEntity<?>createCalendar(Authentication authentication, CalendarsDto calendarsDto) {
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = customUserDetails.getId();

        Long calendarId= calendarService.createCalendar(userId,calendarsDto);

        return ResponseEntity.ok(calendarId);
    }


}
