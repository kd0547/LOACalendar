package com.guild.calendar.service;

import com.guild.calendar.dto.RaidPlanDto;
import com.guild.calendar.entity.RaidPlan;
import com.guild.calendar.repository.RaidPlanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RaidPlanService {

    private final RaidPlanRepository raidPlanRepository;

    @Transactional(readOnly = true)
    public RaidPlanDto viewRaidPlan(Long userId, Long calendarId) {

        List<RaidPlan> raidPlans = raidPlanRepository.findByCalendarAndUsers(calendarId,userId);

        return null;
    }
}
