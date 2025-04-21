package com.guild.calendar.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class DiscodeInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    private Calendar calendar;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    @Column(nullable = false)
    private Long server_owner_id;

    @Column(nullable = false)
    private Long discode_server_id;

    @Column(nullable = false)
    private Long channel_id;

    private String alarmYN;
}
