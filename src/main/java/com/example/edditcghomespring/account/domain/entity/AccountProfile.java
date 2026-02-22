package com.example.edditcghomespring.account.domain.entity;

import com.example.edditcghomespring.account.domain.vo.Email;
import com.example.edditcghomespring.account.domain.vo.Nickname;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "account_profile", uniqueConstraints = {
        @UniqueConstraint(columnNames = "email")
})
public class AccountProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Email email;

    @Embedded
    private Nickname nickname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    public AccountProfile(Email email, Nickname nickname) {
        if (email == null) throw new IllegalArgumentException("Email은 필수입니다.");
        if (nickname == null) throw new IllegalArgumentException("Nickname은 필수입니다.");
        this.email = email;
        this.nickname = nickname;
    }

    public void setAccount(Account account) {
        if (account == null) throw new IllegalArgumentException("Account은 필수입니다.");
        this.account = account;
    }
}
