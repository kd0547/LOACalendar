package com.guild.calendar.dto;

import com.guild.calendar.constant.ErrorCode;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponse {
    private ErrorCode errorCode;
    private String error;
    private String message;
    private LocalDateTime timestamp;

    public ErrorResponse() {}
    public ErrorResponse(ErrorCode errorCode, String error, String message, LocalDateTime timestamp) {
        this.errorCode = errorCode;
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }


}
