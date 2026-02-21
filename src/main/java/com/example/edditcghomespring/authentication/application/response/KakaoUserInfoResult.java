package com.example.edditcghomespring.authentication.application.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoUserInfoResult {

    private final String id;
    private final String email;
    private final String nickname;
    private final String profileImage;
}
