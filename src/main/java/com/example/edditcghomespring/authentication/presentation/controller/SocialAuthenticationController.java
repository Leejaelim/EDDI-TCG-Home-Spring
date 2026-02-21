package com.example.edditcghomespring.authentication.presentation.controller;

import com.example.edditcghomespring.authentication.application.SocialAuthenticationUseCase;
import com.example.edditcghomespring.authentication.application.response.KakaoAccessTokenResponse;
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

    private final SocialAuthenticationUseCase useCase;

    @PostMapping("/link")
    public ResponseEntity<OAuthLinkResponseForm> requestOAuthLink(
            @RequestBody OAuthLinkRequestForm form
    ) {
        OAuthLinkResult result =
                useCase.generateOAuthLink(form.toGenerateOAuthLinkCommand());

        return ResponseEntity.ok(
                OAuthLinkResponseForm.from(result)
        );
    }

    @GetMapping("/request-access-token-after-redirection")
    public ResponseEntity<KakaoAccessTokenResponse> requestAccessToken(
            @RequestParam String code
    ) {
        return ResponseEntity.ok(
                useCase.requestKakaoAccessToken(code)
        );
    }

}
