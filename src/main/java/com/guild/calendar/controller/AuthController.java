package com.guild.calendar.controller;

import com.guild.calendar.dto.MemberDto;
import com.guild.calendar.dto.SigninDto;
import com.guild.calendar.service.AuthService;
import com.guild.calendar.jwt.JwtTokenProvider;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody SigninDto signinDTO) {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signinDTO.getEmail(),signinDTO.getPassword())
        );

        String token = jwtTokenProvider.generateToken(authentication);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/sign-in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Registration successful"),
            @ApiResponse(responseCode = "403", description = "Email already exists"),
            @ApiResponse(responseCode = "400", description = "Invalid request format"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
