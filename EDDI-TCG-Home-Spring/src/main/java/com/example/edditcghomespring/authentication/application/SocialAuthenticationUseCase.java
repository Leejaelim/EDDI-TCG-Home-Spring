package com.example.edditcghomespring.authentication.application;

import com.example.edditcghomespring.authentication.application.request.GenerateOAuthLinkCommand;
import com.example.edditcghomespring.authentication.application.response.OAuthLinkResult;

public interface SocialAuthenticationUseCase {

    OAuthLinkResult generateOAuthLink(GenerateOAuthLinkCommand command);
}
