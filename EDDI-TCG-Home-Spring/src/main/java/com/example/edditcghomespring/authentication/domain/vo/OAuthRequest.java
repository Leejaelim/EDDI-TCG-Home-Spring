package com.example.edditcghomespring.authentication.domain.vo;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OAuthRequest {

    private final String state;

    public static OAuthRequest create(String state) {
        return new OAuthRequest(state);
    }
}

