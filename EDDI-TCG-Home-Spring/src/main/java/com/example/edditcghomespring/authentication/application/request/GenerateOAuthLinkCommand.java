package com.example.edditcghomespring.authentication.application.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GenerateOAuthLinkCommand {

    private final String provider;
}
