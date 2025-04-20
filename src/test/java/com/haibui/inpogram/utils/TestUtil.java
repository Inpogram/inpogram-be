package com.haibui.inpogram.utils;

import com.haibui.inpogram.security.JwtService;
import jakarta.servlet.http.Cookie;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class TestUtil {
    public static void mockJwtAuth(JwtService jwtService, UserDetailsService uds, String jwt, String email) {
        when(jwtService.extractUsername(jwt)).thenReturn(email);
        when(jwtService.isTokenValid(eq(jwt), any(UserDetails.class))).thenReturn(true);
        when(uds.loadUserByUsername(email))
                .thenReturn(new User(email, "password", Collections.
                        singletonList(new SimpleGrantedAuthority("ROLE_USER")))); // Add roles if needed
    }

    public static RequestPostProcessor withJwtCookie(String jwt) {
        return request -> {
            request.setCookies(new Cookie("access_token", jwt));
            return request;
        };
    }
}
