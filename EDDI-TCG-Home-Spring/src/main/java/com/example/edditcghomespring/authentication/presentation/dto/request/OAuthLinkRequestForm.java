package com.example.edditcghomespring.authentication.presentation.dto.request;

import com.example.edditcghomespring.authentication.application.request.GenerateOAuthLinkCommand;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OAuthLinkRequestForm {

    private final String provider;

    public GenerateOAuthLinkCommand toGenerateOAuthLinkCommand() {
        return new GenerateOAuthLinkCommand(provider);
    }
}
