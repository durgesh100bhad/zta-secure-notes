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
import com.zta.zta_backend.service.RateLimiterService;
import com.zta.zta_backend.service.SecurityLogService;
import io.github.bucket4j.Bucket;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.MessageDigest;
import java.time.Instant;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class HmacFilter extends OncePerRequestFilter {
    private final ClientAppRepository repo;
    private final RateLimiterService rateLimiterService;
    private final SecurityLogService logService;

    public HmacFilter(ClientAppRepository repo,
                      RateLimiterService rateLimiterService,
                      SecurityLogService logService) {
        this.repo = repo;
        this.rateLimiterService = rateLimiterService;
        this.logService = logService;
    }

    @Override
protected void doFilterInternal(HttpServletRequest request,
                                HttpServletResponse response,
                                FilterChain chain)
        throws ServletException, IOException {

    // Skip auth endpoints
    if (request.getRequestURI().startsWith("/auth")
            || request.getRequestURI().startsWith("/oauth2")) {
        chain.doFilter(request, response);
        return;
    }

    CachedBodyHttpServletRequest wrapped = new CachedBodyHttpServletRequest(request);

    String clientId = request.getHeader("X-Client-Id");
    String timestamp = request.getHeader("X-Timestamp");
    String signature = request.getHeader("X-Signature");

    // 🔍 DEBUG START
    System.out.println("\n---- NEW REQUEST ----");
    System.out.println("URI: " + request.getRequestURI());
    System.out.println("ClientId: " + clientId);
    System.out.println("Timestamp: " + timestamp);
    System.out.println("Auth Header: " + request.getHeader("Authorization"));

    //  Header validation
    if (clientId == null || timestamp == null || signature == null) {
        System.out.println("❌ FAILED: MISSING HEADERS");
        response.sendError(401, "Missing HMAC headers");
        return;
    }

    //  Rate limiting (FIRST DEFENSE)
    Bucket bucket = rateLimiterService.resolveBucket(clientId);
    System.out.println("Tokens BEFORE consume: " + bucket.getAvailableTokens());

    if (!bucket.tryConsume(1)) {
        System.out.println("❌ FAILED: RATE LIMIT");
//        response.sendError(429, "Too many requests");
//        return;
        response.setStatus(429);
        response.setContentType("application/json");
        response.setHeader("Content-Type", "application/json");
        response.getWriter().write("{\"error\": \"Too many requests\"}");
        response.getWriter().flush();
        return;
    }

    // Timestamp validation
    long now = Instant.now().getEpochSecond();
    long reqTime;

    try {
        reqTime = Long.parseLong(timestamp);
    } catch (Exception e) {
        System.out.println("❌ FAILED: INVALID TIMESTAMP FORMAT");
        response.sendError(400, "Invalid timestamp");
        return;
    }

    System.out.println("Now: " + now + " | RequestTime: " + reqTime);

    if (Math.abs(now - reqTime) > 300) {
        System.out.println("❌ FAILED: TIMESTAMP EXPIRED (Replay Attack)");
        response.sendError(401, "Expired request");
        return;
    }

    //  Client validation
    ClientApp client = repo.findByClientId(clientId).orElse(null);

    if (client == null || !client.isActive()) {
        System.out.println("❌ FAILED: INVALID CLIENT");
        response.sendError(401, "Invalid client");
        return;
    }

    //  Signature validation
    String data = SignatureBuilder.build(wrapped, timestamp);
    String expected = HmacUtil.generate(data, client.getClientSecret());

    System.out.println("Expected Sig: " + expected);
    System.out.println("Received Sig: " + signature);

    if (!MessageDigest.isEqual(expected.getBytes(), signature.getBytes())) {
        System.out.println("❌ FAILED: INVALID SIGNATURE");
        response.sendError(401, "Invalid signature");
        return;
    }

    //  SUCCESS (HMAC + Rate limit passed)
    System.out.println("✅ PASSED HMAC + RATE LIMIT");

    // Continue to Spring Security (JWT validation happens AFTER this)
    chain.doFilter(wrapped, response);
}
}
