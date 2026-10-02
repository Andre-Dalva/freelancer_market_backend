package com.andretk.backend1.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateUserRequest {
    private String name;
    private String email;
    private String location;
    private String phoneNumber;
}
