package org.example.service.impl;

import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldEncodePasswordAndSaveUser() {
        UserDto dto = new UserDto("john", "plainpass", "john@example.com");
        User savedUser = new User();
        savedUser.setUsername("john");
        savedUser.setEmail("john@example.com");
        savedUser.setPassword("encodedpass");

        when(passwordEncoder.encode("plainpass")).thenReturn("encodedpass");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = userService.createUser(dto);

        assertEquals("john", result.getUsername());
        assertEquals("encodedpass", result.getPassword());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserByUsername_ShouldReturnUserIfExists() {
        User user = new User();
        user.setUsername("jane");

        when(userRepository.findByUsername("jane")).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserByUsername("jane");

        assertTrue(result.isPresent());
        assertEquals("jane", result.get().getUsername());
    }

    @Test
    void getAllUser_ShouldReturnUserList() {
        List<User> users = List.of(new User(), new User());
        when(userRepository.findAll()).thenReturn(users);

        List<User> result = userService.getAllUser();

        assertEquals(2, result.size());
    }

    @Test
    void updateUser_ShouldUpdateOnlyNonNullFields() {
        UserDto dto = new UserDto("john", null, "new@mail.com");
        User user = new User();
        user.setUsername("john");
        user.setEmail("old@mail.com");
        user.setPassword("oldpass");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        Optional<User> result = userService.updateUser(dto);

        assertTrue(result.isPresent());
        assertEquals("new@mail.com", result.get().getEmail());
        assertEquals("oldpass", result.get().getPassword()); // password wasn’t changed
    }

    @Test
    void updateUser_ShouldReturnEmptyIfUsernameIsNull() {
        UserDto dto = new UserDto(null, "pass", "email");
        Optional<User> result = userService.updateUser(dto);
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteUserByUsername_ShouldDeleteIfUserFound() {
        User user = new User();
        user.setUserId(10L);
        user.setUsername("john");

        when(userRepository.findByUsername("john")).thenReturn(Optional.of(user));

        userService.deleteUserByUsername("john");

        verify(userRepository).deleteById(10L);
    }

    @Test
    void deleteUserByUsername_ShouldNotDeleteIfUserNotFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());

        userService.deleteUserByUsername("ghost");

        verify(userRepository, never()).deleteById(any());
    }

}