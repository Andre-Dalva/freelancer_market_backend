package com.andretk.backend1.dtos;

import com.andretk.backend1.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;

@AllArgsConstructor
@Getter
public class CompleteUserDto {
    private Long id;
    private String name;
    private String email;
    private UserRole role;
    private String location;
    private String phoneNumber;
    private Instant createdAt;
}
