package com.email.writer.service;

import com.email.writer.dto.AuthResponse;
import com.email.writer.dto.LoginRequest;
import com.email.writer.dto.RegisterRequest;
import com.email.writer.entity.RefreshToken;
import com.email.writer.entity.User;
import com.email.writer.repository.UserRepository;
import com.email.writer.security.JwtService;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;

        private final JwtService jwtService;

        private final RefreshTokenService refreshTokenService;

        public AuthServiceImpl(
                        UserRepository userRepository,

                        PasswordEncoder passwordEncoder,

                        JwtService jwtService,

                        RefreshTokenService refreshTokenService) {

                this.userRepository = userRepository;

                this.passwordEncoder = passwordEncoder;

                this.jwtService = jwtService;

                this.refreshTokenService = refreshTokenService;
        }

        @Override
        @Transactional
        public AuthResponse register(
                        RegisterRequest request) {

                if (userRepository.existsByEmail(
                                request.getEmail())) {

                        throw new IllegalStateException(
                                        "Email already registered");
                }

                User user = new User();

                user.setFullName(
                                request.getFullName());

                user.setEmail(
                                request.getEmail());

                user.setPassword(
                                passwordEncoder.encode(
                                                request.getPassword()));

                userRepository.save(user);

                String accessToken = jwtService.generateToken(
                                user.getEmail());

                RefreshToken refreshToken = refreshTokenService
                                .createRefreshToken(
                                                user.getEmail());

                return new AuthResponse(
                                accessToken,
                                refreshToken.getToken());
        }

        @Override
        public AuthResponse login(
                        LoginRequest request) {

                User user = userRepository
                                .findByEmail(request.getEmail())

                                .orElseThrow(() -> new BadCredentialsException(
                                                "Invalid email or password"));

                boolean passwordMatches = passwordEncoder.matches(
                                request.getPassword(),
                                user.getPassword());

                if (!passwordMatches) {

                        throw new BadCredentialsException(
                                        "Invalid email or password");
                }

                String accessToken = jwtService.generateToken(
                                user.getEmail());

                RefreshToken refreshToken = refreshTokenService
                                .createRefreshToken(
                                                user.getEmail());

                return new AuthResponse(
                                accessToken,
                                refreshToken.getToken());
        }

        @Override
        public AuthResponse refresh(
                        String refreshToken) {

                RefreshToken token = refreshTokenService
                                .validateToken(refreshToken);

                String newAccessToken = jwtService.generateToken(
                                token.getEmail());

                return new AuthResponse(
                                newAccessToken,
                                refreshToken);
        }

        @Override
        @Transactional
        public void logout(String email) {

                refreshTokenService.deleteByEmail(email);
        }
}