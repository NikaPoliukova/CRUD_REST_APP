package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.UserDto;
import org.example.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/registration")
public class AuthController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody UserDto userDto) {
        return userService.getUserByUsername(userDto.username())
                .map(user -> ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists"))
                .orElseGet(() -> {
                    var createdUser = userService.createUser(userDto);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(createdUser.getUsername() + " successfully registry");
                });
    }
}