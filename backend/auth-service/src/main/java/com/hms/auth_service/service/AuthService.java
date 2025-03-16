package com.hms.auth_service.service;

import com.hms.auth_service.client.UserServiceClient;
import com.hms.auth_service.dto.RegisterDto;
import com.hms.auth_service.dto.TokenDto;
import com.hms.auth_service.dto.UserDto;
import com.hms.auth_service.exec.WrongCredentialsException;
import com.hms.auth_service.request.LoginRequest;
import com.hms.auth_service.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;

    public TokenDto login(LoginRequest request) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authenticate.isAuthenticated())
            return TokenDto
                    .builder()
                    .token(jwtService.generateToken(request.getUsername()))
                    .build();
        else throw new WrongCredentialsException("Wrong credentials");
    }

    public ResponseEntity<UserDto> getUserDetail(String username){
        return userServiceClient.getUserByUsername(username);
    }
    public RegisterDto register(RegisterRequest request) {
        return userServiceClient.save(request).getBody();
    }
}