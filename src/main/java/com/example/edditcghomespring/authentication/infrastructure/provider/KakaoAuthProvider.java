package com.example.edditcghomespring.authentication.infrastructure.provider;

import com.example.edditcghomespring.authentication.application.response.KakaoUserInfoResult;
import com.example.edditcghomespring.authentication.domain.service.SocialAuthProvider;
import com.example.edditcghomespring.authentication.domain.vo.OAuthRequest;
import com.example.edditcghomespring.authentication.domain.vo.SocialProviderType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class KakaoAuthProvider implements SocialAuthProvider {

    private static final String AUTHORIZE_URL = "https://kauth.kakao.com/oauth/authorize";
    private static final String RESPONSE_TYPE = "code";

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    @Value("${kakao.token-request-uri}")
    private String tokenRequestUri;

    @Value("${kakao.user-info-request-uri}")
    private String userInfoRequestUri;

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

    public Map<String, Object> requestAccessToken(String code) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("Kakao authorization code 필요");
        }

        String url = UriComponentsBuilder.fromUriString(tokenRequestUri)
                .queryParam("grant_type", "authorization_code")
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("code", code)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        Map<String, Object> response = restTemplate.postForObject(url, entity, Map.class);

        if (response == null || !response.containsKey("access_token")) {
            throw new RuntimeException("Kakao Access Token 발급 실패");
        }

        return response;
    }

    public KakaoUserInfoResult requestUserInfo(String accessToken) {

        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalArgumentException("Access Token 필요");
        }

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(accessToken);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        Map<String, Object> response =
                restTemplate.postForObject(userInfoRequestUri, entity, Map.class);

        if (response == null || !response.containsKey("id")) {
            throw new RuntimeException("Kakao 사용자 정보 조회 실패");
        }

        Map<String, Object> kakaoAccount = (Map<String, Object>) response.get("kakao_account");
        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");

        return new KakaoUserInfoResult(
                String.valueOf(response.get("id")),
                (String) kakaoAccount.get("email"),
                (String) profile.get("nickname"),
                (String) profile.get("profile_image_url")
        );
    }
}
