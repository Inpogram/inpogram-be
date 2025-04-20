package com.haibui.inpogram.utils;

import com.haibui.inpogram.security.JwtService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestConfiguration
public class JwtTestConfig {

    @Bean
    public JwtService jwtService() {
        JwtService mock = mock(JwtService.class);
        when(mock.extractUsername(anyString())).thenReturn("user@example.com");
        when(mock.isTokenValid(anyString(), any())).thenReturn(true);
        return mock;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> new User(username, "password", Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
