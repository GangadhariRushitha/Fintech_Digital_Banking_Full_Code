//package com.example.FinTech_Digital_Banking.Entity;
//
//
//import jakarta.persistence.*;
//import lombok.*;
//
//
//import java.math.BigDecimal;
//import java.time.OffsetDateTime;
//import java.util.UUID;
//
//
//
//@Entity
//@Table(name = "ledger_entry")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class LedgerEntry {
//@Id
//@GeneratedValue(strategy = GenerationType.IDENTITY)
//private Long ledgerId;
//
//
//@Column(nullable = false, columnDefinition = "uuid")
//private UUID transactionId;
//
//
//@Column(nullable = false)
//private OffsetDateTime entryTimestamp = OffsetDateTime.now();
//
//
//@Column(nullable = false)
//private Long accountId;
//
//
//@Column
//private Long relatedAccountId;
//
//
//@Column(nullable = false, length = 10)
//private String entryType; // "DEBIT" or "CREDIT"
//
//
//@Column(nullable = false, precision = 30, scale = 8)
//private BigDecimal amount;
//
//
//@Column(nullable = false, length = 3)
//private String currency = "USD";
//
//
//@Column(columnDefinition = "text")
//private String description;
//
//
//@Column
//private Long createdBy;
//}



package com.example.FinTech_Digital_Banking.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ledger_entry")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LedgerEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ledgerId;

    @Column(nullable = false, columnDefinition = "uuid")
    private UUID transactionId;

    @Builder.Default
    @Column(nullable = false)
    private OffsetDateTime entryTimestamp = OffsetDateTime.now();

    @Column(nullable = false)
    private Long accountId;

    @Column
    private Long relatedAccountId;

    @Column(nullable = false, length = 10)
    private String entryType; // "DEBIT" or "CREDIT"

    @Column(nullable = false, precision = 30, scale = 8)
    private BigDecimal amount;

    @Builder.Default
    @Column(nullable = false, length = 3)
    private String currency = "USD";

    @Column(columnDefinition = "text")
    private String description;

    @Column
    private Long createdBy;
}
