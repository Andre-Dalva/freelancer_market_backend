package com.andretk.backend1.controllers;

import com.andretk.backend1.dtos.CompleteUserDto;
import com.andretk.backend1.dtos.JwtResponse;
import com.andretk.backend1.dtos.LoginRequest;
import com.andretk.backend1.dtos.UserDto;
import com.andretk.backend1.exceptions.UnauthorizedException;
import com.andretk.backend1.services.AuthService;
import com.andretk.backend1.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest request){

        var authLoginDto = authService.login(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authLoginDto.getCookie().toString())
                .body(new JwtResponse(authLoginDto.getAccessToken()));
    }

    @PatchMapping("/logout")
    public ResponseEntity<Void> logout(){
        var logOut = authService.clearRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, logOut.toString())
                .build();
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken){
        if (refreshToken == null || refreshToken.isBlank())
            throw new UnauthorizedException("Refresh token is missing");

        var newAccessToken = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(new JwtResponse(newAccessToken));
    }


    @GetMapping("/me")
    public ResponseEntity<CompleteUserDto> me(){
        var userDto = userService.currentUser();

        return ResponseEntity.ok(userDto);
    }
}
