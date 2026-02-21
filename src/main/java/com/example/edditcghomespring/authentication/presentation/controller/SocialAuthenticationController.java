package com.example.edditcghomespring.authentication.presentation.controller;

import com.example.edditcghomespring.account.application.AccountProfileUseCase;
import com.example.edditcghomespring.authentication.application.SocialAuthenticationUseCase;
import com.example.edditcghomespring.authentication.application.response.KakaoAccessTokenResponse;
import com.example.edditcghomespring.authentication.application.response.KakaoUserInfoResult;
import com.example.edditcghomespring.authentication.application.response.OAuthLinkResult;
import com.example.edditcghomespring.authentication.presentation.dto.request.OAuthLinkRequestForm;
import com.example.edditcghomespring.authentication.presentation.dto.response.OAuthLinkResponseForm;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/oauth")
@RequiredArgsConstructor
public class SocialAuthenticationController {

    private final SocialAuthenticationUseCase authenticationUseCase;
    private final AccountProfileUseCase accountProfileUseCase;

    @PostMapping("/link")
    public ResponseEntity<OAuthLinkResponseForm> requestOAuthLink(
            @RequestBody OAuthLinkRequestForm form
    ) {
        OAuthLinkResult result =
                authenticationUseCase.generateOAuthLink(form.toGenerateOAuthLinkCommand());

        return ResponseEntity.ok(
                OAuthLinkResponseForm.from(result)
        );
    }

    @GetMapping("/request-access-token-after-redirection")
    public ResponseEntity<KakaoAccessTokenResponse> requestAccessToken(
            @RequestParam String code
    ) {
        KakaoAccessTokenResponse tokenResponse = authenticationUseCase.requestKakaoAccessToken(code);
        KakaoUserInfoResult userInfo = authenticationUseCase.requestKakaoUserInfo(tokenResponse.getAccessToken());

        if (userInfo.getEmail() == null || userInfo.getEmail().isBlank()) {
            throw new IllegalStateException("Kakao에서 이메일 정보를 가져올 수 없습니다.");
        }
        
        boolean isSignedUp = accountProfileUseCase.isSignedUp(userInfo.getEmail());
        System.out.println("가입 여부: " + (isSignedUp ? "기존 회원" : "신규 회원"));

        System.out.println("ID: " + userInfo.getId());
        System.out.println("Nickname: " + userInfo.getNickname());
        System.out.println("Email: " + userInfo.getEmail());

        return ResponseEntity.ok(tokenResponse);
    }

}
