package com.guild.calendar.dto;

import com.guild.calendar.constant.Role;
import com.guild.calendar.entity.Users;
import lombok.Data;

@Data
public class MemberDto {
    private String email;
    private Role role;

    public MemberDto() {}

    public MemberDto(Users users) {
        this.email = users.getEmail();
        this.role = users.getRole();
    }
}
