package com.example.edditcghomespring.account.presentation.controller;

import com.example.edditcghomespring.account.application.AccountUseCase;
import com.example.edditcghomespring.account.application.response.AccountRegisterResponse;
import com.example.edditcghomespring.account.presentation.dto.request.AccountCreateRequestForm;
import com.example.edditcghomespring.account.presentation.dto.response.AccountRegisterResponseForm;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountUseCase accountUseCase;

    @PostMapping("/sign-up")
    public ResponseEntity<AccountRegisterResponseForm> signUpWithTemporaryToken(
            @CookieValue("temporaryToken") String temporaryToken,
            @RequestBody AccountCreateRequestForm form,
            HttpServletResponse response
    ) {
        System.out.println("Received temporaryToken: " + temporaryToken);
        System.out.println("Received form: " + form);
        // 1. 회원가입 서비스 호출
        AccountRegisterResponse result = accountUseCase.registerAccount(
                temporaryToken,
                form.toAccountCreateRequest(),
                form.toAccountProfileCreateRequest()
        );

        // 2. userToken 쿠키 생성
        ResponseCookie cookie = ResponseCookie.from("userToken", result.getUserToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(6 * 60 * 60) // 6시간
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());

        // 3. 기존 임시 토큰 쿠키 제거
        ResponseCookie deleteTempCookie = ResponseCookie.from("temporaryToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0) // 즉시 만료
                .sameSite("Strict")
                .build();
        response.addHeader("Set-Cookie", deleteTempCookie.toString());

        // 4. Response 반환
        return ResponseEntity.ok(AccountRegisterResponseForm.from(result));
    }
}