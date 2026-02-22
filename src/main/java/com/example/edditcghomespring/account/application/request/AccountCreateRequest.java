package com.example.edditcghomespring.account.application.request;

import com.example.edditcghomespring.account.domain.vo.LoginType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountCreateRequest {
    private final LoginType loginType;
}
