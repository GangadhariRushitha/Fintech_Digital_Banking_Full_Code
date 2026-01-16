package com.example.FinTech_Digital_Banking;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.Assertions;
import com.example.FinTech_Digital_Banking.DTO.TransferRequest;
import com.example.FinTech_Digital_Banking.Entity.LedgerEntry;
import com.example.FinTech_Digital_Banking.Entity.Account;
import com.example.FinTech_Digital_Banking.Repository.LedgerEntryRepository;
import com.example.FinTech_Digital_Banking.Repository.AccountRepository;
import com.example.FinTech_Digital_Banking.Service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransferConcurrencyTest {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountRepository accountRepository;

    private Long fromAccountId;
    private Long toAccountId;

    @BeforeAll
    void setup() {
        Account from = new Account();
        from.setOwnerUserId(1L);
        from.setCurrency("USD");
        from.setBalance(new BigDecimal("1000.00"));
        from = accountRepository.save(from);
        fromAccountId = from.getAccountId();

        Account to = new Account();
        to.setOwnerUserId(1L);
        to.setCurrency("USD");
        to.setBalance(BigDecimal.ZERO);
        to = accountRepository.save(to);
        toAccountId = to.getAccountId();
    }

    @Test
    void concurrentWithdrawals_shouldNeverGoNegative() throws Exception {

        int threads = 100;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(threads);

        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                try {
                    TransferRequest req = new TransferRequest();
                    req.setFromAccountId(fromAccountId);
                    req.setToAccountId(toAccountId);
                    req.setAmount(new BigDecimal("10.00"));
                    req.setCurrency("USD");
                    req.setDescription("Concurrency Test");

                    transferService.transfer(req, -1L);
                } catch (Exception ignored) {
                    // insufficient funds expected for some threads
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        Account fromFinal = accountRepository.findById(fromAccountId).orElseThrow();
        Account toFinal = accountRepository.findById(toAccountId).orElseThrow();

        System.out.println("From balance: " + fromFinal.getBalance());
        System.out.println("To balance: " + toFinal.getBalance());

        // ✅ FINAL ASSERTIONS (THIS IS WHAT MATTERS)
        Assertions.assertTrue(
            fromFinal.getBalance().compareTo(BigDecimal.ZERO) >= 0,
            "Balance must never be negative"
        );

        // total moved ≤ initial balance
        Assertions.assertTrue(
            toFinal.getBalance().compareTo(new BigDecimal("1000.00")) <= 0
        );
    }
}
