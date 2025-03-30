package com.haibui.inpogram.security;

import com.haibui.inpogram.models.entities.User;
import com.haibui.inpogram.models.enums.AuthProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserPrincipal implements OAuth2User, UserDetails {

    private final Collection<? extends GrantedAuthority> authorities;

    @Getter
    private final int id;

    @Getter
    private final String providerUsername;

    @Getter
    private final String email;

    private final String password;

    @Getter
    private final AuthProvider provider;

    @Getter
    private final String providerId;

    @Getter
    private final String profileImageUrl;

    @Setter
    private Map<String, Object> attributes;

    public UserPrincipal(
            int id,
            String providerUsername,
            String email,
            String password,
            AuthProvider provider,
            String providerId,
            String profileImageUrl,
            Collection<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.providerUsername = providerUsername;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.providerId = providerId;
        this.profileImageUrl = profileImageUrl;
        this.authorities = authorities;
    }

    public static UserPrincipal build(User user) {
        List<GrantedAuthority> authorities = Collections.
                singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        return new UserPrincipal(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getProvider(),
                user.getProviderId(),
                user.getProfileImageUrl(),
                authorities
        );
    }

    public static UserPrincipal build(User user, Map<String, Object> attributes) {
        UserPrincipal userPrincipal = UserPrincipal.build(user);
        userPrincipal.setAttributes(attributes);
        return userPrincipal;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return String.valueOf(id);
    }
}
