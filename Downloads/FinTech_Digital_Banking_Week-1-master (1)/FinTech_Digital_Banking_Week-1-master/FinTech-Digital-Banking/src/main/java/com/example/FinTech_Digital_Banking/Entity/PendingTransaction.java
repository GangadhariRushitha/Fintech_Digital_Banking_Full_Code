/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author admin
 */
package com.example.FinTech_Digital_Banking.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "pending_transaction")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PendingTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID transactionId;

    private Long fromAccountId;
    private Long toAccountId;

    @Column(nullable = false, precision = 30, scale = 8)
    private BigDecimal amount;

    private Long performedByUserId;

    @Column(nullable = false, length = 6)
    private String otp;

    private OffsetDateTime expiryTime;

    private boolean verified = false;
}
