package com.guild.calendar.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class SigninDTO {

    @NotEmpty
    private String email;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;
}
