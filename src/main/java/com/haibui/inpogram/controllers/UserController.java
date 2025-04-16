package com.haibui.inpogram.controllers;

import com.haibui.inpogram.models.dtos.UserDTO;
import com.haibui.inpogram.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
//    @Secured({"ROLE_LECTURER", "ROLE_ADMIN"})
    public ResponseEntity<UserDTO> getUser(Authentication authentication) {
        UserDTO userDTO = userService.getUserData(authentication);
        return ResponseEntity.ok(userDTO);
    }
}
