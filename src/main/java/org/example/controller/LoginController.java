package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.AuthRequest;
import org.example.dto.AuthResponse;
import org.example.security.JwtUtils;
import org.example.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class LoginController {

    private final JwtUtils jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.userName(), request.password())
        );
        var user = userService.getUserByUsername(request.userName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(new AuthResponse(jwtUtil.generateToken(user)));
    }
}
