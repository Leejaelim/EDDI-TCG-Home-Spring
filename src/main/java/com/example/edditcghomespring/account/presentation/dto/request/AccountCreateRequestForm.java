package com.example.edditcghomespring.account.presentation.dto.request;

import com.example.edditcghomespring.account.application.request.AccountCreateRequest;
import com.example.edditcghomespring.account.application.request.AccountProfileCreateRequest;
import com.example.edditcghomespring.account.domain.vo.LoginType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class AccountCreateRequestForm {

    private final String email;
    private final String nickname;
    private final LoginType loginType;

    // UseCase에 전달할 DTO
    public AccountCreateRequest toAccountCreateRequest() {
        return new AccountCreateRequest(
                loginType
        );
    }

    // AccountProfile 생성용 DTO
    public AccountProfileCreateRequest toAccountProfileCreateRequest() {
        return new AccountProfileCreateRequest(
                nickname,
                email
        );
    }
}
