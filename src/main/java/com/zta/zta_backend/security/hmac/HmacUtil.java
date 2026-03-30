/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.security.hmac;

/**
 *
 * @author hcdc
 */
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class HmacUtil {

    private static final String HMAC_ALGO = "HmacSHA256";

    public static String generate(String data, String secret) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGO);
            SecretKeySpec key = new SecretKeySpec(secret.getBytes(), HMAC_ALGO);
            mac.init(key);

            byte[] raw = mac.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(raw);

        } catch (Exception e) {
            throw new RuntimeException("HMAC error", e);
        }
    }
}
