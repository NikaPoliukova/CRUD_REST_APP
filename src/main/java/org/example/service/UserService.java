package org.example.service;


import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public User createUser(UserDto dto) {
        User user = new User();
        user.setUsername(dto.username());
        user.setEmail(dto.email());
        user.setPassword(dto.password());
        return userRepository.save(user);
    }

    public Optional<User> getById(Long id) {
        return userRepository.findById(id);
    }

    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    public Optional<User> updateUser(Long id, UserDto dto) {
        return userRepository.findById(id).map(user -> {
            user.setUsername(dto.username());
            user.setEmail(dto.email());
            user.setPassword(dto.password());
            return userRepository.save(user);
        });
    }

    public void deleteUserById(Long id) {
        userRepository.deleteById(id);
    }
}
