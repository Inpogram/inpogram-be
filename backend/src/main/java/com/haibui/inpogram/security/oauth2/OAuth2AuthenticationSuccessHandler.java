package com.haibui.inpogram.security.oauth2;

import com.haibui.inpogram.configurations.ApplicationProperties;
import com.haibui.inpogram.constants.AuthErrorConstants;
import com.haibui.inpogram.exceptions.BadRequestException;
import com.haibui.inpogram.models.entities.User;
import com.haibui.inpogram.models.enums.Role;
import com.haibui.inpogram.models.repositories.UserRepository;
import com.haibui.inpogram.security.JwtService;
import com.haibui.inpogram.security.UserPrincipal;
import com.haibui.inpogram.utils.CookieUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;

import static com.haibui.inpogram.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.ACCESS_TOKEN_COOKIE_NAME;
import static com.haibui.inpogram.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.AUTH_FLOW_COOKIE_NAME;
import static com.haibui.inpogram.security.oauth2.HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI_PARAM_COOKIE_NAME;

@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final JwtService jwtService;
    private final ApplicationProperties applicationProperties;
    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;
    private final UserRepository userRepository;

    @Autowired
    @Lazy
    public OAuth2AuthenticationSuccessHandler(JwtService jwtService,
                                              ApplicationProperties applicationProperties,
                                              HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository,
                                              UserRepository userRepository) {
        this.jwtService = jwtService;
        this.applicationProperties = applicationProperties;
        this.httpCookieOAuth2AuthorizationRequestRepository = httpCookieOAuth2AuthorizationRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String targetUrl = determineTargetUrl(request, response, authentication);
        String flow = determineAuthFlow(request);
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if (authentication instanceof OAuth2AuthenticationToken) {
            String email = userPrincipal.getEmail();
            Optional<User> existingUser = userRepository.findByEmail(email);

            if ("signup".equals(flow)) {
                if (existingUser.isEmpty()) {
                    User newUser = new User();
                    newUser.setProvider(userPrincipal.getProvider());
                    newUser.setProviderId(userPrincipal.getProviderId());
                    newUser.setUsername(userPrincipal.getProviderUsername());
                    newUser.setEmail(userPrincipal.getEmail());
                    newUser.setProfileImageUrl(userPrincipal.getProfileImageUrl());
                    newUser.setRole(Role.USER);
                    userRepository.save(newUser);
                }
                // If user already exists, also log that user in
            } else if ("login".equals(flow)) {
                if (existingUser.isEmpty()) {
                    throw new OAuth2AuthenticationException(AuthErrorConstants.ACCOUNT_NOT_FOUND_ERROR);
                }
                // If user already exists, log that user in
            } else {
                targetUrl = getDefaultTargetUrl();
            }
        } else {
            targetUrl = getDefaultTargetUrl();
        }

        if (response.isCommitted()) {
            logger.debug("Response has already been committed. Unable to redirect to " + targetUrl);
            return;
        }
        String token = jwtService.generateToken(authentication);
        CookieUtils.addCookie(request, response, ACCESS_TOKEN_COOKIE_NAME, token, 86400000);
        clearAuthenticationAttributes(request, response);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    /*
     * Returns a redirect url
     * */
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Optional<String> redirectUri = CookieUtils.getCookie(request, REDIRECT_URI_PARAM_COOKIE_NAME)
                .map(Cookie::getValue);
        if (redirectUri.isPresent() && !isAuthorizedRedirectUri(redirectUri.get())) {
            throw new BadRequestException("Sorry! We've got an Unauthorized Redirect URI and can't proceed with the authentication");
        }
        String targetUrl = redirectUri.orElse(getDefaultTargetUrl());
        return UriComponentsBuilder.fromUriString(targetUrl)
                .build().toUriString();
    }

    protected String determineAuthFlow(HttpServletRequest request) {
        Optional<String> authFlow = CookieUtils.getCookie(request, AUTH_FLOW_COOKIE_NAME)
                .map(Cookie::getValue);
        return authFlow.orElse("");
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    private boolean isAuthorizedRedirectUri(String uri) {
        URI clientRedirectUri = URI.create(uri);

        return applicationProperties.getOAuth2().getAuthorizedRedirectUris()
                .stream()
                .anyMatch(authorizedRedirectUri -> {
                    // Only validate host and port. Let the clients use different paths if they want to
                    URI authorizedURI = URI.create(authorizedRedirectUri);
                    return authorizedURI.getHost().equalsIgnoreCase(clientRedirectUri.getHost())
                            && authorizedURI.getPort() == clientRedirectUri.getPort();
                });
    }


}
