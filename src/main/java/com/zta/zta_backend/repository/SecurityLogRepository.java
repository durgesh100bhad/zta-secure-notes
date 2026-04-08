/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.zta.zta_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zta.zta_backend.entity.SecurityLog;
/**
 *
 * @author hcdc
 */
public interface SecurityLogRepository extends JpaRepository<SecurityLog, Long> {
}
