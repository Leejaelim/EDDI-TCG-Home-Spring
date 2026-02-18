package com.example.edditcghomespring.authentication.infrastructure.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Validated
@ConfigurationProperties(prefix = "social.kakao")
public class KakaoOAuthProperties {

    @NotBlank
    private String clientId;

    @NotBlank
    private String redirectUri;

    @NotBlank
    private String authorizeUrl;
}
