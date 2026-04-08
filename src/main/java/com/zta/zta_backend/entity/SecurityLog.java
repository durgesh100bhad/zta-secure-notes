/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hcdc
 */
@Entity
@Setter
@Getter
public class SecurityLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientId;
    private String userId;
    private String endpoint;
    private String method;

    private String status;   // ALLOWED / BLOCKED
    private String reason;   // RATE_LIMIT / INVALID_SIGNATURE / SUCCESS

    private Long timestamp;
}
