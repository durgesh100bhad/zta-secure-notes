/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.security.hmac;

/**
 *
 * @author hcdc
 */
import com.zta.zta_backend.entity.ClientApp;
import com.zta.zta_backend.repository.ClientAppRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.MessageDigest;
import java.time.Instant;

@Component
public class HmacFilter extends OncePerRequestFilter {

    private final ClientAppRepository repo;

    public HmacFilter(ClientAppRepository repo) {
        this.repo = repo;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip public endpoints
        if (path.startsWith("/auth")
                || path.startsWith("/oauth2")
                || path.startsWith("/api/v1/clients")) {   //  allow client registration
            chain.doFilter(request, response);
            return;
        }

        // Wrap request for reading body multiple times
        CachedBodyHttpServletRequest wrapped =
                new CachedBodyHttpServletRequest(request);

        String clientId = request.getHeader("X-Client-Id");
        String timestamp = request.getHeader("X-Timestamp");
        String signature = request.getHeader("X-Signature");

        // 1. Validate headers
        if (clientId == null || timestamp == null || signature == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Missing HMAC headers");
            return;
        }

        //  2. Validate timestamp (anti-replay)
        long now = Instant.now().getEpochSecond();
        long reqTime;

        try {
            reqTime = Long.parseLong(timestamp);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid timestamp format");
            return;
        }

        if (Math.abs(now - reqTime) > 300) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Request expired (possible replay attack)");
            return;
        }

        //  Fetch client from DB
        ClientApp client = repo.findByClientId(clientId).orElse(null);

        if (client == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Client not registered");
            return;
        }

        if (!client.isActive()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Client inactive");
            return;
        }

        //  4. Rebuild signature
        String data = SignatureBuilder.build(wrapped, timestamp);
        String expectedSignature =
                HmacUtil.generate(data, client.getClientSecret());

        //  5. Constant-time comparison (secure)
        if (!MessageDigest.isEqual(
                expectedSignature.getBytes(),
                signature.getBytes())) {

            response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid signature");
            return;
        }

        //  6. All checks passed
        chain.doFilter(wrapped, response);
    }
}