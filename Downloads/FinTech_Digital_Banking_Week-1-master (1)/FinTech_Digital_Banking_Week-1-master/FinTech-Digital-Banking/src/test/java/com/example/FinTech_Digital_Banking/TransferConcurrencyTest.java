//package com.example.FinTech_Digital_Banking;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.junit.jupiter.api.Assertions;
//import com.example.FinTech_Digital_Banking.DTO.TransferRequest;
//import com.example.FinTech_Digital_Banking.Entity.Ledger;
//import com.example.FinTech_Digital_Banking.Repository.LedgerRepository;
//import com.example.FinTech_Digital_Banking.Repository.AccountRepository;
//import com.example.FinTech_Digital_Banking.Service.TransferService;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.math.BigDecimal;
//import java.util.List;
//import java.util.concurrent.*;
//
//@SpringBootTest
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//public class TransferConcurrencyTest {
//
//    @Autowired
//    private LedgerRepository ledgerRepository;
//
//    @Autowired
//    private AccountRepository accountRepository;
//
//    @Autowired
//    private TransferService transferService;
//
//    private Long fromAccountId;
//    private Long toAccountId;
//
//    @BeforeAll
//    void setup() {
//        // create two accounts for test (persisted via AccountRepository)
//        var a1 = new com.example.FinTech_Digital_Banking.Entity.Account();
//        a1.setEmail("from@example.com");
//        a1.setDisplayName("From Test");
//        a1 = accountRepository.save(a1);
//        fromAccountId = a1.getId();
//
//        var a2 = new com.example.FinTech_Digital_Banking.Entity.Account();
//        a2.setEmail("to@example.com");
//        a2.setDisplayName("To Test");
//        a2 = accountRepository.save(a2);
//        toAccountId = a2.getId();
//
//        // seed initial balance for fromAccount by inserting a credit in ledger
//        Ledger seed = new Ledger();
//        seed.setAccountId(fromAccountId);
//        seed.setTransactionId(java.util.UUID.randomUUID());
//        seed.setCredit(new BigDecimal("1000.00"));
//        seed.setDebit(BigDecimal.ZERO);
//        seed.setDescription("Seed balance");
//        ledgerRepository.save(seed);
//    }
//
//    @Test
//    void testConcurrentTransfers() throws InterruptedException {
//        int threads = 100;
//        ExecutorService pool = Executors.newFixedThreadPool(20);
//        CountDownLatch latch = new CountDownLatch(threads);
//
//        // each task transfers 10.00 from fromAccount to toAccount
//        for (int i = 0; i < threads; i++) {
//            pool.submit(() -> {
//                try {
//                    TransferRequest r = new TransferRequest();
//                    r.setFromAccountId(fromAccountId);
//                    r.setToAccountId(toAccountId);
//                    r.setAmount(new BigDecimal("10.00"));
//                    r.setDescription("concurrent test");
//                    // IMPORTANT: transferService.transfer will call authenticated user check - in tests you may need to bypass or mock security
//                    // For simplicity, if getAuthenticatedAccountId uses SecurityContext, tests must set a principal.
//                    // Here we assume transfer() does not require authentication in test profile, or you adapt the method to skip ownership check.
//                    transferService.transfer(r);
//                } catch (Exception e) {
//                    // swallow for this test; we will assert final balance
//                } finally {
//                    latch.countDown();
//                }
//            });
//        }
//
//        latch.await();
//        pool.shutdownNow();
//
//        BigDecimal fromFinal = ledgerRepository.getBalance(fromAccountId);
//        BigDecimal toFinal = ledgerRepository.getBalance(toAccountId);
//
//        // from started with 1000 and 100 * 10 = 1000 attempted transfers -> should be 0 if all succeeded
//        System.out.println("fromFinal = " + fromFinal + ", toFinal = " + toFinal);
//
//        Assertions.assertTrue(fromFinal.compareTo(BigDecimal.ZERO) >= 0, "from account should not be negative");
//        // total moved should not exceed initial balance
//        Assertions.assertTrue(fromFinal.compareTo(new BigDecimal("0.00")) >= 0);
//    }
//}
