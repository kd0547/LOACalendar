package com.guild.calendar.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class GuildController {

    //길드 생성
    @PostMapping("guild/new")
    public void create() {

    }
    //길드 수정
    public void update() {

    }
    //길드 삭제
    public void delete() {

    }
    //길드 조회
    public void find() {

    }
}
