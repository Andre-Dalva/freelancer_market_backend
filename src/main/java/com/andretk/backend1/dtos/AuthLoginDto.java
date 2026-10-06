package com.andretk.backend1.dtos;

import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuthLoginDto {
    private String accessToken;
    private Cookie cookie;
}
