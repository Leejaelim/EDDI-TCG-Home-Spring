package com.example.edditcghomespring.authentication.application.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KakaoAccessTokenResponse {

    private final String accessToken;
    private final String refreshToken;
    private final Long expiresIn;

}
