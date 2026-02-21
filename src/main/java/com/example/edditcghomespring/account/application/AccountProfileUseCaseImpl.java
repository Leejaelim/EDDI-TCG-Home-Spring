package com.example.edditcghomespring.account.application;

import com.example.edditcghomespring.account.domain.repository.AccountProfileRepository;
import com.example.edditcghomespring.account.domain.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountProfileUseCaseImpl implements AccountProfileUseCase {
    private final AccountProfileRepository accountProfileRepository;

    @Override
    public boolean isSignedUp(String emailStr) {
        if (emailStr == null || emailStr.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        Email email = new Email(emailStr); // VO로 변환하며 포맷 체크 포함
        return accountProfileRepository.findByEmail(email).isPresent();
    }
}
