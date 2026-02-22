package com.example.edditcghomespring.account.domain.repository;

import com.example.edditcghomespring.account.domain.entity.AccountProfile;
import com.example.edditcghomespring.account.domain.vo.Email;

import java.util.Optional;


public interface AccountProfileRepository {
    boolean existsByEmail(Email email);
    Optional<AccountProfile> findByEmail(Email email);
    <S extends AccountProfile> S save(S profile);
    Long findAccountIdByEmail(Email email);
}
