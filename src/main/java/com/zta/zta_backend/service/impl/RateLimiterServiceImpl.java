/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.service.impl;

import com.zta.zta_backend.service.RateLimiterService;
import io.github.bucket4j.*;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;
import org.springframework.stereotype.Service;

/**
 *
 * @author hcdc
 */
@Service
public class RateLimiterServiceImpl implements RateLimiterService {
   private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

   
    private Bucket createNewBucket() {
    Bandwidth limit = Bandwidth.classic(
            10,
            Refill.greedy(10, Duration.ofMinutes(1))
    );

    return Bucket.builder()
            .addLimit(limit)
            .build();
}

   
    public Bucket resolveBucket(String clientId) {
        return cache.computeIfAbsent(clientId, k -> createNewBucket());
    }
}
