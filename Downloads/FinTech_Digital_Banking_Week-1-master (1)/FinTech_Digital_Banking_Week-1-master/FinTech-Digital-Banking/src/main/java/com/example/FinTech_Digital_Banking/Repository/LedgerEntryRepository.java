package com.example.FinTech_Digital_Banking.Repository;
import com.example.FinTech_Digital_Banking.Entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.UUID;


public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
List<LedgerEntry> findByTransactionId(UUID transactionId);


}