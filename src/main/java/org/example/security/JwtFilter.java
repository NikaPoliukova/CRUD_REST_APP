package org.example.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@RequiredArgsConstructor
@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String token = jwtUtils.extractTokenFromHeader(request);
        String refreshToken = jwtUtils.extractRefreshTokenFromHeader(request);

        if (token != null && jwtUtils.validateToken(token)) {
            setAuthentication(token);
        } else if (token != null && refreshToken != null
                && jwtUtils.isTokenExpired(token)
                && jwtUtils.validateToken(refreshToken)) {

            String newAccessToken = jwtUtils.refreshAccessToken(token);
            setAuthentication(newAccessToken);
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        String userName = jwtUtils.extractUsername(token);
        var authentication = new UsernamePasswordAuthenticationToken(userName, null, Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
