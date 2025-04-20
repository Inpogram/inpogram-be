package com.haibui.inpogram.utils;

import com.haibui.inpogram.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

public abstract class AuthenticatedControllerTestBase {
    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected JwtService jwtService;
    @MockBean protected UserDetailsService userDetailsService;

    protected final String jwt = "test.jwt.token";
    protected final String userEmail = "user@example.com";

    @BeforeEach
    void setupJwt() {
        TestUtil.mockJwtAuth(jwtService, userDetailsService, jwt, userEmail);
    }

    protected RequestPostProcessor withAuth() {
        return TestUtil.withJwtCookie(jwt);
    }
}
