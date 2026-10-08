package com.andretk.backend1.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseCookie;

@AllArgsConstructor
@Data
public class AuthLoginDto {
    private String accessToken;
    private ResponseCookie cookie;
}
