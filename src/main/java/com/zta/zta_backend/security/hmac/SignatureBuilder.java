/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.security.hmac;

/**
 *
 * @author hcdc
 */
import jakarta.servlet.http.HttpServletRequest;

import java.io.BufferedReader;
import java.util.stream.Collectors;

public class SignatureBuilder {

    public static String build(HttpServletRequest request, String timestamp) {

        try {
            String method = request.getMethod();
            String uri = request.getRequestURI();

            String body = new BufferedReader(request.getReader())
                    .lines()
                    .collect(Collectors.joining());

            return method + uri + body + timestamp;

        } catch (Exception e) {
            throw new RuntimeException("Signature build failed", e);
        }
    }
}