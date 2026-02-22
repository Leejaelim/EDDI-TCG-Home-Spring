package com.example.edditcghomespring.account.application;

import com.example.edditcghomespring.account.application.request.AccountCreateRequest;
import com.example.edditcghomespring.account.application.request.AccountProfileCreateRequest;
import com.example.edditcghomespring.account.application.response.AccountRegisterResponse;

public interface AccountUseCase {
    AccountRegisterResponse registerAccount(
            String temporaryToken,
            AccountCreateRequest accountCreateRequest,
            AccountProfileCreateRequest accountProfileCreateRequest
    );
}
