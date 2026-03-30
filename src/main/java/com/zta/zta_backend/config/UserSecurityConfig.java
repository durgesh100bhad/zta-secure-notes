/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.config;

/**
 *
 * @author hcdc
 */
import com.zta.zta_backend.entity.User;
import com.zta.zta_backend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

@Configuration
public class UserSecurityConfig {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepo) {

        return username -> {
            User user = userRepo.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return new org.springframework.security.core.userdetails.User(
                    user.getEmail(),
                    user.getPassword(),
                    List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
            );
        };
    }
}