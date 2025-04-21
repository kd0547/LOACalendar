package com.guild.calendar.dto;

import com.guild.calendar.entity.Calendar;
import lombok.Data;

@Data
public class CalendarsDto {
    private Long id;

    private String subject;

    private String description;

    public static CalendarsDto createCalendarDto(Calendar calendar) {
        CalendarsDto calendarsDto = new CalendarsDto();

        calendarsDto.setId(calendar.getId());
        calendarsDto.setSubject(calendar.getSubject());
        calendarsDto.setDescription(calendar.getDescription());

        return calendarsDto;
    }
}
