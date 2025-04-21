package com.guild.calendar.service;

import com.guild.calendar.dto.CalendarsDto;
import com.guild.calendar.entity.Calendar;
import com.guild.calendar.repository.CalendarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarService {

    private final CalendarRepository calendarRepository;

    @Transactional
    public List<CalendarsDto> viewCalendars(Long userId) {
        List<Calendar> calendars = calendarRepository.findByUserId(userId);

        return calendars.stream()
                .map(CalendarsDto::createCalendarDto)
                .toList();
    }

    @Transactional
    public Long createCalendar(Long userId, CalendarsDto calendarsDto) {
        Calendar calendar = Calendar.createCalendar(userId,calendarsDto);
        calendarRepository.save(calendar);

        return calendar.getId();
    }
}
