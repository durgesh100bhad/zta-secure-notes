/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.security;

/**
 *
 * @author hcdc
 */
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.List;

@Service
public class JwtService {

    private final String SECRET = "mysecretkeymysecretkeymysecret12";

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    public String generateToken(String email) {

        return Jwts.builder()
                .setSubject(email)
                .claim("scope", List.of("notes.read", "notes.write"))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(key)
                .compact();
    }
}
