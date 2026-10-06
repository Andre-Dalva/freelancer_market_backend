package com.andretk.backend1.controllers;

import com.andretk.backend1.dtos.JwtResponse;
import com.andretk.backend1.dtos.LoginRequest;
import com.andretk.backend1.dtos.UserDto;
import com.andretk.backend1.services.AuthService;
import com.andretk.backend1.services.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest request,
                                             HttpServletResponse response){

        var authLoginDto = authService.login(request);
        response.addCookie(authLoginDto.getCookie());

        return ResponseEntity.ok(new JwtResponse(authLoginDto.getAccessToken()));
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refresh(@CookieValue(value = "refreshToken") String refreshToken){
        var newAccessToken = authService.refreshToken(refreshToken);

        return ResponseEntity.ok(new JwtResponse(newAccessToken));
    }


    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        var userDto = userService.me();

        return ResponseEntity.ok(userDto);
    }
}
