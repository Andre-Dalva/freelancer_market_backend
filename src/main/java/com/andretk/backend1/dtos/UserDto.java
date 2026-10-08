package com.andretk.backend1.dtos;

import com.andretk.backend1.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
}
