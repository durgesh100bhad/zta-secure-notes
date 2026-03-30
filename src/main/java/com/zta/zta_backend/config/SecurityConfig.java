/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.config;

/**
 *
 * @author hcdc
 */
import com.zta.zta_backend.security.hmac.HmacFilter;
import io.jsonwebtoken.security.Keys;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
@Configuration
public class SecurityConfig {

    @Autowired
private HmacFilter hmacFilter;
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/oauth2/**","/auth/**").permitAll() 
                .anyRequest().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt
        .decoder(jwtDecoder())
        .jwtAuthenticationConverter(jwtAuthenticationConverter())
            ));

           http.addFilterBefore(hmacFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
    
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {

            List<String> scopes = jwt.getClaimAsStringList("scope");

            if (scopes == null) {
                return List.of();
            }

            return scopes.stream()
                    .map(scope -> new SimpleGrantedAuthority("SCOPE_" + scope))
                    .collect(Collectors.toList());
        });

    return converter;
    }
    
        @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance(); 
    }
    
    @Bean
   public JwtDecoder jwtDecoder() {

       String secret = "mysecretkeymysecretkeymysecret12";

       SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

       return NimbusJwtDecoder.withSecretKey(key).build();
   }
    
   
}