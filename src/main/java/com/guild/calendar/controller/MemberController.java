package com.guild.calendar.controller;

import com.guild.calendar.dto.SigninDTO;
import com.guild.calendar.service.MemberService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private MemberService memberService;


    @PostMapping("/member/sign-in")
    public ResponseEntity<Object> createMember(@RequestBody SigninDTO signinDTO) {
        Long id = memberService.join(signinDTO);

        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("id",id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMap);
    }

}
