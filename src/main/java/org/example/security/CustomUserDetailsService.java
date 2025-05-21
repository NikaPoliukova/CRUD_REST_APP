package org.example.security;

import lombok.RequiredArgsConstructor;
import org.example.service.UserService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userService.getUserByUsername(username)
                .map(user -> User.withUsername(user.getUserName())
                        .password(user.getPassword())
                        .authorities(Collections.singletonList(
                                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
                        ))
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }
}
