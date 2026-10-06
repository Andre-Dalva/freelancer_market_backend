package com.andretk.backend1.services;

import com.andretk.backend1.config.JwtConfig;
import com.andretk.backend1.dtos.AuthLoginDto;
import com.andretk.backend1.dtos.LoginRequest;
import com.andretk.backend1.exceptions.ResourceNotFoundException;
import com.andretk.backend1.exceptions.UnauthorizedException;
import com.andretk.backend1.repositories.UserRepository;
import jakarta.servlet.http.Cookie;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final JwtConfig jwtConfig;

    public AuthLoginDto login(LoginRequest request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow( () -> new ResourceNotFoundException(
                "User not found!"));

        var accessToken = jwtService.generateAccessToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        var cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration()); //7 days
        cookie.setSecure(true);

        return new AuthLoginDto(accessToken,cookie);
    }

    public String refreshToken(String refreshToken){
        if (!jwtService.validateToken(refreshToken)) {
            throw new UnauthorizedException("User not authorized");
        }

        var userId = jwtService.getIdFromToken(refreshToken);
        var user = userRepository.findById(userId).orElseThrow( () -> new ResourceNotFoundException("User not found!"));

        return jwtService.generateAccessToken(user);

    }
}
