package com.example.edditcghomespring.account.infrastructure.repository;

import com.example.edditcghomespring.account.domain.entity.Account;
import com.example.edditcghomespring.account.domain.repository.AccountRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAccountRepository extends JpaRepository<Account, Long>, AccountRepository {

    <S extends Account> S save(S account);
}
