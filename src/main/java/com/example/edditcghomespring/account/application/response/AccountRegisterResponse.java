package com.example.edditcghomespring.account.application.response;

import com.example.edditcghomespring.account.domain.entity.AccountProfile;
import com.example.edditcghomespring.account.domain.vo.LoginType;
import com.example.edditcghomespring.authentication.domain.vo.SocialProviderType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccountRegisterResponse {

    private final String userToken;
    private final String email;
    private final String nickname;
    private final LoginType loginType;

    // AccountProfile + userToken → Response 변환
    public static AccountRegisterResponse from(AccountProfile profile, String userToken) {
        return new AccountRegisterResponse(
                userToken,
                profile.getEmail().getValue(),
                profile.getNickname().getValue(),
                profile.getAccount().getLoginType()
        );
    }
}