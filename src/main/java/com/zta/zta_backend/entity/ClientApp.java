/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.zta.zta_backend.entity;

/**
 *
 * @author hcdc
 */
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "client_apps")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientApp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String clientId;

    @Column(nullable = false)
    private String clientSecret;

    private String appName;

    private boolean active;
}
