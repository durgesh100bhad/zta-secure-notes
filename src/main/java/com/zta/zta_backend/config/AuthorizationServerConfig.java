/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.config;

/**
 *
 * @author hcdc
 */

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.*;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

@Configuration
public class AuthorizationServerConfig {

   // Authorization Server Security (HIGH PRIORITY)
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain authServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer configurer = new OAuth2AuthorizationServerConfigurer();

        http
            .securityMatcher(configurer.getEndpointsMatcher())
            .with(configurer, Customizer.withDefaults())
            .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
            .csrf(csrf -> csrf.ignoringRequestMatchers(configurer.getEndpointsMatcher()));

        return http.build();
    }

    // Client Registration
    @Bean
    public RegisteredClientRepository registeredClientRepository() {

        RegisteredClient client = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("zta-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("notes.read")
                .scope("notes.write")
                .clientSettings(ClientSettings.builder().build())
            .tokenSettings(TokenSettings.builder().build())
                .build();

        return new InMemoryRegisteredClientRepository(client);
    }
}
