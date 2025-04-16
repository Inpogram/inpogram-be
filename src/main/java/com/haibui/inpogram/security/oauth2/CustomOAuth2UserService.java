package com.haibui.inpogram.security.oauth2;

import com.haibui.inpogram.exceptions.OAuth2AuthenticationProcessingException;
import com.haibui.inpogram.models.entities.User;
import com.haibui.inpogram.models.enums.AuthProvider;
import com.haibui.inpogram.models.repositories.UserRepository;
import com.haibui.inpogram.security.UserPrincipal;
import com.haibui.inpogram.security.oauth2.user.OAuth2UserInfo;
import com.haibui.inpogram.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);
        String provider = oAuth2UserRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(
                provider,
                attributes
        );
        User user = userRepository.findByEmail(oAuth2UserInfo.getEmail()).orElse(null);

        if (user != null) {
            // user exists
            if (!user.getProvider().equals(
                    AuthProvider.valueOf(
                            oAuth2UserRequest.getClientRegistration().getRegistrationId()
                    ))
            ) {
                throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " +
                        user.getProvider() + " account. Please use your " + user.getProvider() +
                        " account to login.");
            }
            user = updateExistingUser(user, oAuth2UserInfo);
        } else {
            // user does not exist
            user = buildUser(oAuth2UserRequest, oAuth2UserInfo);
        }

        return UserPrincipal.build(user, oAuth2User.getAttributes());
    }

    private User buildUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();
        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setUsername(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setProfileImageUrl(oAuth2UserInfo.getImageUrl());
        return user;
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setUsername(oAuth2UserInfo.getName());
        existingUser.setProfileImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
