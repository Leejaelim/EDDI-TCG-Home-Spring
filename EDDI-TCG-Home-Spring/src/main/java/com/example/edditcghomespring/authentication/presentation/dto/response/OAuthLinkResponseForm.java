package com.example.edditcghomespring.authentication.presentation.dto.response;

import com.example.edditcghomespring.authentication.application.response.OAuthLinkResult;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuthLinkResponseForm {

    private final String oauthUrl;

    public static OAuthLinkResponseForm from(OAuthLinkResult result) {
        return new OAuthLinkResponseForm(result.getOauthUrl());
    }
}
