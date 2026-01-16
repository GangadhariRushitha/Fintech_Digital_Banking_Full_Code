package com.example.FinTech_Digital_Banking.Entity;


import jakarta.persistence.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
private Long accountId;


@Column(nullable = false)
private Long ownerUserId;


@Column(nullable = false, length = 3)
private String currency = "USD";


@Column(nullable = false, precision = 30, scale = 8)
private BigDecimal balance = BigDecimal.ZERO;


@Column(nullable = false)
private OffsetDateTime createdAt = OffsetDateTime.now();


@Column(nullable = false)
private OffsetDateTime updatedAt = OffsetDateTime.now();
}


