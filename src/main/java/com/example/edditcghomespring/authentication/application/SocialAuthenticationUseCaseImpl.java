package com.example.edditcghomespring.authentication.application;

import com.example.edditcghomespring.authentication.application.request.GenerateOAuthLinkCommand;
import com.example.edditcghomespring.authentication.application.response.KakaoAccessTokenResponse;
import com.example.edditcghomespring.authentication.application.response.OAuthLinkResult;
import com.example.edditcghomespring.authentication.domain.service.SocialAuthProvider;
import com.example.edditcghomespring.authentication.domain.vo.OAuthRequest;
import com.example.edditcghomespring.authentication.domain.vo.SocialProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SocialAuthenticationUseCaseImpl
        implements SocialAuthenticationUseCase {

    private final Map<SocialProviderType, SocialAuthProvider> providerMap;

    @Override
    public OAuthLinkResult generateOAuthLink(GenerateOAuthLinkCommand command) {

        SocialProviderType providerType =
                SocialProviderType.from(command.getProvider());

        SocialAuthProvider provider = providerMap.get(providerType);

        if (provider == null) {
            throw new IllegalArgumentException("지원하지 않는 provider입니다.");
        }

        String state = UUID.randomUUID().toString();

        OAuthRequest request = OAuthRequest.create(state);

        String url = provider.generateAuthUrl(request);

        return new OAuthLinkResult(url);
    }

    @Override
    public KakaoAccessTokenResponse requestKakaoAccessToken(String code) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("authorization code 필요");
        }

        SocialAuthProvider kakaoProvider =
                providerMap.get(SocialProviderType.KAKAO);

        if (kakaoProvider == null) {
            throw new IllegalStateException("Kakao Provider 등록 필요");
        }

        Map<String, Object> tokenData =
                kakaoProvider.requestAccessToken(code);

        return new KakaoAccessTokenResponse(
                (String) tokenData.get("access_token"),
                (String) tokenData.get("refresh_token"),
                ((Number) tokenData.get("expires_in")).longValue()
        );
    }
}
