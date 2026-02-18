package com.example.edditcghomespring.authentication.domain.service;

import com.example.edditcghomespring.authentication.domain.vo.OAuthRequest;
import com.example.edditcghomespring.authentication.domain.vo.SocialProviderType;

public interface SocialAuthProvider {

    SocialProviderType getProviderType();

    String generateAuthUrl(OAuthRequest request);
}

