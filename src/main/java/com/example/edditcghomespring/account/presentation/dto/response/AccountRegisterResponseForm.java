package com.example.edditcghomespring.account.presentation.dto.response;

import com.example.edditcghomespring.account.application.response.AccountRegisterResponse;
import com.example.edditcghomespring.account.domain.vo.LoginType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountRegisterResponseForm {
    private final String email;
    private final String nickname;
    private final LoginType loginType;

    public static AccountRegisterResponseForm from(AccountRegisterResponse response) {
        return new AccountRegisterResponseForm(
                response.getEmail(),
                response.getNickname(),
                response.getLoginType()
        );
    }
}
