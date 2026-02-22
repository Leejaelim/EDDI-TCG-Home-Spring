package com.example.edditcghomespring.account.infrastructure.repository;

import com.example.edditcghomespring.account.domain.entity.AccountProfile;
import com.example.edditcghomespring.account.domain.repository.AccountProfileRepository;
import com.example.edditcghomespring.account.domain.vo.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaAccountProfileRepository extends JpaRepository<AccountProfile, Long>, AccountProfileRepository {
    boolean existsByEmail(Email email);
    Optional<AccountProfile> findByEmail(Email email);
    <S extends AccountProfile> S save(S profile);

    @Query("SELECT ap.account.id FROM AccountProfile ap WHERE ap.email = :email")
    Long findAccountIdByEmail(@Param("email") Email email);
}
