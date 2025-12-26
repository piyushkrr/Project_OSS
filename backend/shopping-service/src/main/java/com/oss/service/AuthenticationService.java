package com.oss.service;

import com.oss.dto.AuthenticationResponse;
import com.oss.dto.LoginRequest;
import com.oss.dto.RegisterRequest;
import com.oss.entity.Role;
import com.oss.entity.User;
import com.oss.repository.UserRepository;
import com.oss.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository repository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;
        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                User user = User.builder()
                                .firstName(request.getFirstName())
                                .lastName(request.getLastName())
                                .email(request.getEmail())
                                .password(passwordEncoder.encode(request.getPassword()))
                                .phoneNumber(request.getPhoneNumber())
                                .address(request.getAddress())
                                .role(request.getRole() != null ? request.getRole() : Role.USER)
                                .build();
                repository.save(user);
                var jwtToken = jwtUtil.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .role(user.getRole().name())
                                .build();
        }

        public AuthenticationResponse authenticate(LoginRequest request) {
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                var user = repository.findByEmail(request.getEmail())
                                .orElseThrow();
                var jwtToken = jwtUtil.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .role(user.getRole().name())
                                .build();
        }

        public User updateProfile(User user, RegisterRequest request) {
                user.setFirstName(request.getFirstName());
                user.setLastName(request.getLastName());
                user.setPhoneNumber(request.getPhoneNumber());
                user.setAddress(request.getAddress());
                if (request.getPassword() != null && !request.getPassword().isEmpty()) {
                        user.setPassword(passwordEncoder.encode(request.getPassword()));
                }
                return repository.save(user);
        }
}
