/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.controller;

/**
 *
 * @author hcdc
 */
import com.zta.zta_backend.repository.UserRepository;
import com.zta.zta_backend.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.zta.zta_backend.security.JwtService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password) {

        //  Fetch user
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate password 
        if (!user.getPassword().equals(password)) {
            throw new RuntimeException("Invalid password");
        }

        //  Generate JWT with USER identity
        return jwtService.generateToken(user.getEmail());
    }
}