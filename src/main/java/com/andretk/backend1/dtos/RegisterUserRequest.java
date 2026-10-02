package com.andretk.backend1.dtos;

import com.andretk.backend1.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterUserRequest {
    @NotBlank(message = "name is required")
    private String name;

    @NotBlank(message = "email is required")
    @Email
    private String email;

    @NotBlank(message = "password is required")
    @Size(min = 6, max = 25, message = "password  must be between 6-25 characters")
    private String passwordHash;

    @NotBlank(message = "Role cannot be null")
    private String role;

    private String location;
    private String phoneNumber;
}
