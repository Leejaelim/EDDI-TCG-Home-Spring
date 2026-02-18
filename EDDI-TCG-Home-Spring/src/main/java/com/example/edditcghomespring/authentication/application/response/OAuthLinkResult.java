package com.example.edditcghomespring.authentication.application.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuthLinkResult {

    private final String oauthUrl;
}
