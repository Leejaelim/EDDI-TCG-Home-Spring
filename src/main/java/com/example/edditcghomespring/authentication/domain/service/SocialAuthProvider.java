package com.example.edditcghomespring.authentication.domain.service;

import com.example.edditcghomespring.authentication.domain.vo.OAuthRequest;
import com.example.edditcghomespring.authentication.domain.vo.SocialProviderType;

import java.util.Map;

public interface SocialAuthProvider {

    SocialProviderType getProviderType();

    String generateAuthUrl(OAuthRequest request);

    Map<String, Object> requestAccessToken(String code);
}

