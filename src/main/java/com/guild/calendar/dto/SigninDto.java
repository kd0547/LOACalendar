package com.guild.calendar.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SigninDto {

    @NotEmpty
    private String email;

    private String username;

    @NotEmpty
    private String password;


}
