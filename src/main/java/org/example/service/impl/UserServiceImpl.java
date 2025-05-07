package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    public User createUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(passwordEncoder.encode(dto.password()));
        return userRepository.save(user);
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> updateUser(UserDto dto) {
        return userRepository.findByUsername(dto.username()).map(user -> {
            user.setUsername(dto.username());
            user.setEmail(dto.email());
            user.setPassword(dto.password());
            return userRepository.save(user);
        });
    }

    public void deleteUserByUsername(String username) {
        userRepository.findByUsername(username)
                .ifPresent(user -> {
                    userRepository.deleteById(user.getUserId());
                });
    }
}
