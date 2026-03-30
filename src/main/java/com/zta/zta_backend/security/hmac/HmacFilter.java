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
import jakarta.servlet.*;
import jakarta.servlet.http.*;
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

        //  skip login & oauth
        if (request.getRequestURI().startsWith("/auth")
                || request.getRequestURI().startsWith("/oauth2")) {
            chain.doFilter(request, response);
            return;
        }

        CachedBodyHttpServletRequest wrapped = new CachedBodyHttpServletRequest(request);

        String clientId = request.getHeader("X-Client-Id");
        String timestamp = request.getHeader("X-Timestamp");
        String signature = request.getHeader("X-Signature");

        if (clientId == null || timestamp == null || signature == null) {
            response.sendError(401, "Missing HMAC headers");
            return;
        }

        long now = Instant.now().getEpochSecond();
        long reqTime = Long.parseLong(timestamp);

        if (Math.abs(now - reqTime) > 300) {
            response.sendError(401, "Expired request");
            return;
        }

        ClientApp client = repo.findByClientId(clientId).orElse(null);

        if (client == null || !client.isActive()) {
            response.sendError(401, "Invalid client");
            return;
        }

        String data = SignatureBuilder.build(wrapped, timestamp);
        String expected = HmacUtil.generate(data, client.getClientSecret());

        if (!MessageDigest.isEqual(expected.getBytes(), signature.getBytes())) {
            response.sendError(401, "Invalid signature");
            return;
        }

        chain.doFilter(wrapped, response);
    }
}
