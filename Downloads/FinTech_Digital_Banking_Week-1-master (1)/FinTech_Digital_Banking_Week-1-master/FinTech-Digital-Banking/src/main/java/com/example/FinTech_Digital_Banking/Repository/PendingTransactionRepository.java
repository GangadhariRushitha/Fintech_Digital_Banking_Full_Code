package com.example.FinTech_Digital_Banking.Repository;

import com.example.FinTech_Digital_Banking.Entity.PendingTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PendingTransactionRepository
        extends JpaRepository<PendingTransaction, Long> {

    Optional<PendingTransaction>
    findByTransactionIdAndOtpAndVerifiedFalse(UUID transactionId, String otp);
}
