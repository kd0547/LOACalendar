package com.guild.calendar.entity;

import com.guild.calendar.constant.AlarmStatus;
import com.guild.calendar.constant.AlarmType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class DiscordAlarmLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private DiscodeInfo discodeInfo;

    @ManyToOne
    private RaidPlan raidPlan;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType = AlarmType.DISCORD_CHANNEL;

    private LocalDateTime sentAt;

    @Enumerated(EnumType.STRING)
    private AlarmStatus status; // SUCCESS, FAILURE

    private String failReason;

    private int retryCount = 0;
}
