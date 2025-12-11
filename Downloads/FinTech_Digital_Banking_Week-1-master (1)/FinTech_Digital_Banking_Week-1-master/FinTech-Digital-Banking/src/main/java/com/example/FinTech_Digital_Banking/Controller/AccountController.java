package com.example.FinTech_Digital_Banking.Controller;

import com.example.FinTech_Digital_Banking.Entity.Account;
import com.example.FinTech_Digital_Banking.Repository.AccountRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//import com.example.FinTech_Digital_Banking.Security.CustomUserDetailsService;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
@RestController
@RequestMapping("/api")
public class AccountController {

    private final AccountRepository accountRepository;

    public AccountController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // List all accounts (for frontend)
    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> listAccounts() {
        return ResponseEntity.ok(accountRepository.findAll());
    }
  
    // Create an account (for testing)
    @PostMapping("/accounts")
    public ResponseEntity<Account> createAccount(@RequestBody CreateAccountDto dto) {

        Account a = Account.builder()
                .ownerUserId(dto.ownerUserId()) // ✅ FIXED
                .currency(dto.currency() == null ? "USD" : dto.currency()) // ✅ FIXED
                .balance(dto.initialBalance() == null ? BigDecimal.ZERO : dto.initialBalance()) // ✅ FIXED
                .createdAt(OffsetDateTime.now())
                .updatedAt(OffsetDateTime.now())
                .build();

        Account saved = accountRepository.save(a);
        return ResponseEntity.ok(saved);
    }

    public static record CreateAccountDto(
            Long ownerUserId,
            String currency,
            BigDecimal initialBalance
    ) {}
}
