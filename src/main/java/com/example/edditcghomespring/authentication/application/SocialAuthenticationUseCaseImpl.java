package com.example.edditcghomespring.authentication.application;

import com.example.edditcghomespring.account.domain.repository.AccountProfileRepository;
import com.example.edditcghomespring.account.domain.vo.Email;
import com.example.edditcghomespring.authentication.application.request.GenerateOAuthLinkCommand;
import com.example.edditcghomespring.authentication.application.response.KakaoAccessTokenResponse;
import com.example.edditcghomespring.authentication.application.response.KakaoUserInfoResult;
import com.example.edditcghomespring.authentication.application.response.OAuthLinkResult;
import com.example.edditcghomespring.authentication.application.response.SocialLoginResult;
import com.example.edditcghomespring.authentication.domain.service.SocialAuthProvider;
import com.example.edditcghomespring.authentication.domain.vo.OAuthRequest;
import com.example.edditcghomespring.authentication.domain.vo.SocialProviderType;
import com.example.edditcghomespring.authentication.infrastructure.provider.KakaoAuthProvider;
import com.example.edditcghomespring.redis_cache.repository.RedisCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SocialAuthenticationUseCaseImpl
        implements SocialAuthenticationUseCase {

    private final Map<SocialProviderType, SocialAuthProvider> providerMap;
    private final RedisCacheRepository redisCacheRepository;
    private final AccountProfileRepository accountProfileRepository;

    private static final Duration TEMP_TOKEN_TTL = Duration.ofMinutes(5);

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

    @Override
    public KakaoUserInfoResult requestKakaoUserInfo(String accessToken) {
        SocialAuthProvider kakaoProvider = providerMap.get(SocialProviderType.KAKAO);

        if (kakaoProvider == null) {
            throw new IllegalStateException("Kakao Provider 등록 필요");
        }

        // 타입 캐스팅 필요
        if (!(kakaoProvider instanceof KakaoAuthProvider)) {
            throw new IllegalStateException("Kakao Provider 타입 오류");
        }

        return ((KakaoAuthProvider) kakaoProvider).requestUserInfo(accessToken);
    }

    @Override
    public SocialLoginResult issueTemporaryToken(
            String email,
            String nickname,
            SocialProviderType loginType,
            String socialAccessToken
    ) {

        String temporaryToken = UUID.randomUUID().toString();

        redisCacheRepository.setKeyAndValue(
                temporaryToken,
                socialAccessToken,
                TEMP_TOKEN_TTL
        );

        return new SocialLoginResult(
                true,
                email,
                nickname,
                loginType,
                temporaryToken
        );
    }

    @Override
    public SocialLoginResult loginAfterRedirect(
            SocialProviderType providerType,
            String code
    ) {

        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("authorization code 필요");
        }

        SocialAuthProvider provider = providerMap.get(providerType);

        if (provider == null) {
            throw new IllegalStateException("Provider 등록 필요");
        }

        // Access Token 요청
        Map<String, Object> tokenData =
                provider.requestAccessToken(code);

        String accessToken = (String) tokenData.get("access_token");

        if (accessToken == null || accessToken.isBlank()) {
            throw new IllegalStateException("AccessToken 발급 실패");
        }

        // User Info 조회 (카카오 전용 캐스팅)
        if (!(provider instanceof KakaoAuthProvider)) {
            throw new IllegalStateException("Kakao Provider 타입 오류");
        }

        KakaoUserInfoResult userInfo =
                ((KakaoAuthProvider) provider).requestUserInfo(accessToken);

        Email email = new Email(userInfo.getEmail());

        // 가입 여부 판단
        boolean isSignedUp = accountProfileRepository.existsByEmail(email);

        // 기존 회원이면 바로 로그인 처리 (임시 토큰 없음)
        if (isSignedUp) {
            String userToken = UUID.randomUUID().toString();

            Long accountId = accountProfileRepository.findAccountIdByEmail(email);

            return new SocialLoginResult(
                    false,                 // 신규 아님
                    email.getValue(),
                    userInfo.getNickname(),
                    providerType,
                    userToken
            );
        }

        // 신규 회원이면 임시 토큰 발급
        return issueTemporaryToken(
                email.getValue(),
                userInfo.getNickname(),
                providerType,
                accessToken
        );
    }
}
