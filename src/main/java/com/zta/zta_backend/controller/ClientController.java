/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.controller;

import com.zta.zta_backend.repository.ClientAppRepository;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.zta.zta_backend.entity.ClientApp;
/**
 *
 * @author hcdc
 */
@RestController
@RequestMapping("/api/v1/clients")
public class ClientController {

    private final ClientAppRepository clientRepo;

    public ClientController(ClientAppRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    @PostMapping("/register")
    public ClientApp registerClient(@RequestBody Map<String, String> request) {

        String clientId = request.get("clientId");
        String clientSecret = request.get("clientSecret");

        ClientApp client = new ClientApp();
        client.setClientId(clientId);
        client.setClientSecret(clientSecret);
        client.setActive(true);
        client.setAppName("Test Client");

        return clientRepo.save(client);
    }
}