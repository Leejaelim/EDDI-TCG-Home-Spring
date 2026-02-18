package com.example.edditcghomespring.authentication.infrastructure.config;

import com.example.edditcghomespring.authentication.domain.service.SocialAuthProvider;
import com.example.edditcghomespring.authentication.domain.vo.SocialProviderType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class SocialAuthProviderConfig {

    @Bean
    public Map<SocialProviderType, SocialAuthProvider> providerMap(
            List<SocialAuthProvider> providers
    ) {
        return providers.stream()
                .collect(Collectors.toMap(
                        SocialAuthProvider::getProviderType,
                        Function.identity()
                ));
    }
}

