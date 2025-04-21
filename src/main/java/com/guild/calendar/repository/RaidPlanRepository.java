package com.guild.calendar.repository;

import com.guild.calendar.entity.RaidPlan;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RaidPlanRepository extends JpaRepository<RaidPlan,Long> {

    @Query(value = "select r.* from calendar c " +
            "inner join raid_plan r on r.calendar_id = c.id " +
            "where c.users_id = :userId and r.calendar_id = :calendarId",
            nativeQuery = true)
    public List<RaidPlan> findByCalendarAndUsers(@Param("calendarId") Long calendarId,
                                                 @Param("userId") Long userId);


}
