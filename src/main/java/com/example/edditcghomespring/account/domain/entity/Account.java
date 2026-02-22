package com.example.edditcghomespring.account.domain.entity;

import com.example.edditcghomespring.account.domain.vo.LoginType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "account")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "login_type", nullable = false)
    private LoginType loginType;

    public Account(LoginType loginType) {
        if (loginType == null) throw new IllegalArgumentException("LoginType은 필수입니다.");
        this.loginType = loginType;
    }
}
