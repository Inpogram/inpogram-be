package com.haibui.inpogram.security.oauth2.user;

import com.haibui.inpogram.exceptions.OAuth2AuthenticationProcessingException;
import com.haibui.inpogram.models.enums.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString())) {
            // registrationId means provider, such as "google", "facebook"
            return new GoogleOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
        }
    }
}
