/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.zta.zta_backend.repository;

/**
 *
 * @author hcdc
 */
import com.zta.zta_backend.entity.ClientApp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientAppRepository extends JpaRepository<ClientApp, Long> {

    Optional<ClientApp> findByClientId(String clientId);
}
