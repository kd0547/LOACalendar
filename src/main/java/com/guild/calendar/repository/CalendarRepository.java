package com.guild.calendar.repository;


import com.guild.calendar.entity.Calendar;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CalendarRepository extends JpaRepository<Calendar,Long> {


    @Query("SELECT c from Calendar c where c.users.id = :userId")
    public List<Calendar> findByUserId(@Param("userId") long userId);
}
