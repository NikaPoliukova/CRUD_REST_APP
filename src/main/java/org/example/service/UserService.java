package org.example.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


public interface UserService {
    User createUser(UserDto dto);

    //    Optional<User> getById(Long id);
    Optional<User> getUserByUsername(String username);

    List<User> getAllUser();

    Optional<User> updateUser(UserDto dto);

    void deleteUserByUsername(String username);
}
