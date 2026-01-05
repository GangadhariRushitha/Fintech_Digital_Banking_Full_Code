package com.example.FinTech_Digital_Banking.Repository;
import com.example.FinTech_Digital_Banking.Entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;


public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
List<LedgerEntry> findByTransactionId(UUID transactionId);


@Query("""
    SELECT l FROM LedgerEntry l
    WHERE l.accountId = :accountId
    AND l.entryTimestamp BETWEEN :start AND :end
""")
List<LedgerEntry> findMonthlyEntries(
    @Param("accountId") Long accountId,
    @Param("start") OffsetDateTime start,
    @Param("end") OffsetDateTime end
);


}