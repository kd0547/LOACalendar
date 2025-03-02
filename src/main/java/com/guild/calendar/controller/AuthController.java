package com.guild.calendar.controller;

import com.guild.calendar.dto.SigninDto;
import com.guild.calendar.service.AuthService;
import com.guild.calendar.util.JwtUtil;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;

    /**
     *
     * @param signinDTO
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody SigninDto signinDTO) {
        Long memberId = authService.login(signinDTO);
        String token = jwtUtil.generateToken(memberId);

        return ResponseEntity.ok(token);
    }

    /**
     * 회원가입
     * @param signinDTO
     * @return
     */
    @PostMapping("/sign-in")
    public ResponseEntity<Object> createMember(@RequestBody SigninDto signinDTO) {
        Long id = authService.join(signinDTO);

        Map<String,Object> responseMap = new HashMap<>();
        responseMap.put("id",id);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(responseMap);
    }
    //회원수정

    //회원삭제



}
