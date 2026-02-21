package com.example.edditcghomespring.authentication.application;

import com.example.edditcghomespring.authentication.application.request.GenerateOAuthLinkCommand;
import com.example.edditcghomespring.authentication.application.response.KakaoAccessTokenResponse;
import com.example.edditcghomespring.authentication.application.response.OAuthLinkResult;

import java.util.Map;

public interface SocialAuthenticationUseCase {

    OAuthLinkResult generateOAuthLink(GenerateOAuthLinkCommand command);
    KakaoAccessTokenResponse requestKakaoAccessToken(String code);
}
