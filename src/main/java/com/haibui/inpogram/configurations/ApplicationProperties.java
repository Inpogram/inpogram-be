package com.haibui.inpogram.configurations;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "application")
@Getter
public class ApplicationProperties {
    private final Auth auth = new Auth();
    private final OAuth2 oAuth2 = new OAuth2();


    @Getter
    @Setter
    public static class Auth {
        private String secretKey;
        private long expiration;
    }

    @Getter
    public static class OAuth2 {
        private List<String> authorizedRedirectUris = new ArrayList<>();

        public OAuth2 authorizedRedirectUris(List<String> authorizedRedirectUris) {
            this.authorizedRedirectUris = authorizedRedirectUris;
            return this;
        }
    }
}
