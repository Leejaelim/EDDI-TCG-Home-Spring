package com.example.edditcghomespring.account.application.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountProfileCreateRequest {
    private final String nickname;
    private final String email;
}
