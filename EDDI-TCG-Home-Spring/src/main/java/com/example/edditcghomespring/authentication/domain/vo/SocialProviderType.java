package com.example.edditcghomespring.authentication.domain.vo;

public enum SocialProviderType {
    KAKAO;

    public static SocialProviderType from(String value) {
        try {
            return valueOf(value.toUpperCase());
        } catch (Exception e) {
            throw new IllegalArgumentException("지원하지 않는 provider입니다.");
        }
    }
}
