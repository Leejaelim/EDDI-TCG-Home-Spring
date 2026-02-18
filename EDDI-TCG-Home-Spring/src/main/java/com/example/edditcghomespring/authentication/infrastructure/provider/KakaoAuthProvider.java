package com.example.edditcghomespring.authentication.infrastructure.provider;

import com.example.edditcghomespring.authentication.domain.service.SocialAuthProvider;
import com.example.edditcghomespring.authentication.domain.vo.OAuthRequest;
import com.example.edditcghomespring.authentication.domain.vo.SocialProviderType;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class KakaoAuthProvider implements SocialAuthProvider {

    private static final String AUTHORIZE_URL =
            "https://kauth.kakao.com/oauth/authorize";

    private static final String RESPONSE_TYPE = "code";

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Override
    public SocialProviderType getProviderType() {
        return SocialProviderType.KAKAO;
    }

    @Override
    public String generateAuthUrl(OAuthRequest request) {

        if (clientId == null || clientId.isBlank()) {
            throw new IllegalStateException("Kakao clientId 설정 필요");
        }

        if (redirectUri == null || redirectUri.isBlank()) {
            throw new IllegalStateException("Kakao redirectUri 설정 필요");
        }

        return UriComponentsBuilder
                .fromUriString(AUTHORIZE_URL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", RESPONSE_TYPE)
                .queryParam("state", request.getState())
                .build()
                .toUriString();
    }
}
