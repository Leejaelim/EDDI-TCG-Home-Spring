package com.example.edditcghomespring.authentication.presentation.controller;

import com.example.edditcghomespring.account.application.AccountProfileUseCase;
import com.example.edditcghomespring.authentication.application.SocialAuthenticationUseCase;
import com.example.edditcghomespring.authentication.application.response.KakaoAccessTokenResponse;
import com.example.edditcghomespring.authentication.application.response.KakaoUserInfoResult;
import com.example.edditcghomespring.authentication.application.response.OAuthLinkResult;
import com.example.edditcghomespring.authentication.application.response.SocialLoginResult;
import com.example.edditcghomespring.authentication.domain.vo.SocialProviderType;
import com.example.edditcghomespring.authentication.presentation.dto.request.OAuthLinkRequestForm;
import com.example.edditcghomespring.authentication.presentation.dto.response.OAuthLinkResponseForm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
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
    public ResponseEntity<SocialLoginResult> requestAccessToken(
            @RequestParam String code,
            HttpServletResponse response
    ) {
        SocialLoginResult result =
                authenticationUseCase.loginAfterRedirect(
                        SocialProviderType.KAKAO,
                        code
                );

        if (result.getToken() != null) {
            long maxAge = result.isNewUser() ? 5 * 60 : 12 * 60 * 60;

            ResponseCookie cookie = ResponseCookie.from(
                            result.isNewUser() ? "temporaryToken" : "userToken",
                            result.getToken())
                    .httpOnly(true)
                    .secure(true)
                    .path("/")
                    .maxAge(maxAge)
                    .sameSite("Strict")
                    .build();

            response.addHeader("Set-Cookie", cookie.toString());
        }

        // 3. Body는 최소화 (optional)
        return ResponseEntity.ok(result);
    }

}
