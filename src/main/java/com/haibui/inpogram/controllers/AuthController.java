package com.haibui.inpogram.controllers;

import com.haibui.inpogram.utils.CookieUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.haibui.inpogram.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.ACCESS_TOKEN_COOKIE_NAME;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, ACCESS_TOKEN_COOKIE_NAME);
        return ResponseEntity.ok().build();
    }
}
