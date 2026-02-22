package com.example.edditcghomespring.account.domain.repository;

import com.example.edditcghomespring.account.domain.entity.Account;

public interface AccountRepository {
    <S extends Account> S save(S account);
}
