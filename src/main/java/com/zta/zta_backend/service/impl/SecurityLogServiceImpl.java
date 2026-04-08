/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.service.impl;

import com.zta.zta_backend.service.SecurityLogService;

/**
 *
 * @author hcdc
 */

import com.zta.zta_backend.dto.CreateNoteRequest;
import com.zta.zta_backend.dto.NoteResponse;
import com.zta.zta_backend.entity.*;
import com.zta.zta_backend.exception.AccessDeniedException;
import com.zta.zta_backend.exception.ResourceNotFoundException;
import com.zta.zta_backend.repository.*;
import com.zta.zta_backend.service.NoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityLogServiceImpl implements SecurityLogService {
    
//    @Autowired
//    private SecurityLogRepository repository;
//
//    public void log(String clientId, String userId, String endpoint,
//                    String method, String status, String reason) {
//
//        SecurityLog log = new SecurityLog();
//        log.setClientId(clientId);
//        log.setUserId(userId);
//        log.setEndpoint(endpoint);
//        log.setMethod(method);
//        log.setStatus(status);
//        log.setReason(reason);
//        log.setTimestamp(System.currentTimeMillis());
//
//        repository.save(log);
//    }
    @Override
    public void log(String message) {
        System.out.println("[SECURITY LOG] " + message);
    }
}
