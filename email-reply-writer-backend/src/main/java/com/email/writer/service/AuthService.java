package com.email.writer.service;

import com.email.writer.dto.AuthResponse;
import com.email.writer.dto.LoginRequest;
import com.email.writer.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refresh(String refreshToken);

    void logout(String email);
}