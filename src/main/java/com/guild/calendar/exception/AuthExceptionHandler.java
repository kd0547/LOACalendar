package com.guild.calendar.exception;

import com.guild.calendar.controller.AuthController;
import com.guild.calendar.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice(assignableTypes = AuthController.class)
public class AuthExceptionHandler {

    @ExceptionHandler(EmailDuplicateException.class)
    public ResponseEntity<?> emailDuplicateExceptionHandler(EmailDuplicateException e) {
        ErrorResponse errorResponse = new ErrorResponse(null,"error",e.getMessage(),LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
    public ResponseEntity<?> emailOrPasswordFailExceptionHandler(BadCredentialsException b,UsernameNotFoundException u) {

        String errorMessage = "";

        if(b != null)
            errorMessage = b.getMessage();
        if(u != null)
            errorMessage = u.getMessage();

        ErrorResponse errorResponse = new ErrorResponse(null,"error",errorMessage,LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }



}
