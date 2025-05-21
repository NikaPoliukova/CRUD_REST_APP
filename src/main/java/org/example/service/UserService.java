package org.example.service;


import org.example.dto.UserDto;
import org.example.entity.User;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User createUser(UserDto dto);

    Optional<User> getUserByUsername(String username);

    List<User> getAllUser();

    Optional<User> updateUser(UserDto dto);

    void deleteUserByUsername(String username);
}
