package com.example.edditcghomespring.account.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.Objects;

@Getter
@Embeddable
public class Nickname {

    private String value;

    protected Nickname() {
        // JPA용
    }

    public Nickname(String value) {
        if (value == null || value.length() < 2) {
            throw new IllegalArgumentException("닉네임은 최소 2글자 이상이어야 합니다.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    // equals, hashCode 반드시 구현
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Nickname)) return false;
        Nickname nickname = (Nickname) o;
        return Objects.equals(value, nickname.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}

