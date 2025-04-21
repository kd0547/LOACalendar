package com.guild.calendar.entity;

import com.guild.calendar.constant.AlarmType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class AlarmLog {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Users targetUser;

    @ManyToOne
    private RaidPlan raidPlan;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType; // EMAIL, DISCORD 등

    private LocalDateTime sentAt;
    private String status;
    private String failReason;
    private int retryCount;
}
