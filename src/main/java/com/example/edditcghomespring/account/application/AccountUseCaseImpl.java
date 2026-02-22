package com.example.edditcghomespring.account.application;

import com.example.edditcghomespring.account.application.request.AccountCreateRequest;
import com.example.edditcghomespring.account.application.request.AccountProfileCreateRequest;
import com.example.edditcghomespring.account.application.response.AccountRegisterResponse;
import com.example.edditcghomespring.account.domain.entity.Account;
import com.example.edditcghomespring.account.domain.entity.AccountProfile;
import com.example.edditcghomespring.account.domain.repository.AccountProfileRepository;
import com.example.edditcghomespring.account.domain.repository.AccountRepository;
import com.example.edditcghomespring.account.domain.vo.Email;
import com.example.edditcghomespring.account.domain.vo.Nickname;
import com.example.edditcghomespring.redis_cache.repository.RedisCacheRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountUseCaseImpl implements AccountUseCase {

    private final AccountProfileRepository accountProfileRepository;
    private final AccountRepository accountRepository;
    private final RedisCacheRepository redisCacheRepository;

    @Override
    public AccountRegisterResponse registerAccount(
            String temporaryToken,
            AccountCreateRequest accountCreateRequest,
            AccountProfileCreateRequest accountProfileCreateRequest
    ) {

        // 임시 토큰 검증 (Redis에서 account PK 연결 여부 확인)
        String kakaoAccessToken = redisCacheRepository.getValueByKey(temporaryToken, String.class);
        if (kakaoAccessToken == null) {
            throw new IllegalStateException("유효하지 않은 임시 토큰입니다.");
        }

        // Account + AccountProfile 생성
        Account account = new Account(accountCreateRequest.getLoginType());
        accountRepository.save(account);

        AccountProfile profile = new AccountProfile(
                new Email(accountProfileCreateRequest.getEmail()),
                new Nickname(accountProfileCreateRequest.getNickname())
        );

        profile.setAccount(account);
        accountProfileRepository.save(profile);

        // 사용자 토큰 발급 (UUID)
        String userToken = UUID.randomUUID().toString();

        // userToken -> accountId
        redisCacheRepository.setKeyAndValue(userToken, String.valueOf(account.getId()));
        // accountId -> kakaoAccessToken
        redisCacheRepository.setKeyAndValue("kakao:" + account.getId(), kakaoAccessToken);
        redisCacheRepository.deleteByKey(temporaryToken);

        // 6. Response 생성
        return AccountRegisterResponse.from(profile, userToken);
    }
}