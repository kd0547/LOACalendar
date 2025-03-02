package com.guild.calendar.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;


@Data
public class LoginDto {

    @NotEmpty
    private String email;

    @NotEmpty
    private String password;
}
