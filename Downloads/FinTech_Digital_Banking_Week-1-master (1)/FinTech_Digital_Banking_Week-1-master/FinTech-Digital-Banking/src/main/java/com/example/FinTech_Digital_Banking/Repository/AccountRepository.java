package com.example.FinTech_Digital_Banking.Repository;


import com.example.FinTech_Digital_Banking.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;


import jakarta.persistence.LockModeType;
import java.util.Optional;


public interface AccountRepository extends JpaRepository<Account, Long> {


@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT a FROM Account a WHERE a.accountId = :id")
Optional<Account> findByIdForUpdate(Long id);


Optional<Account> findByOwnerUserId(Long ownerUserId);
}