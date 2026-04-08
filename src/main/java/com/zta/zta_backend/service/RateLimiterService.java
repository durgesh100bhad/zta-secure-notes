/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.zta.zta_backend.service;

import io.github.bucket4j.Bucket;

/**
 *
 * @author hcdc
 */
public interface RateLimiterService {
     Bucket resolveBucket(String clientId);
}
