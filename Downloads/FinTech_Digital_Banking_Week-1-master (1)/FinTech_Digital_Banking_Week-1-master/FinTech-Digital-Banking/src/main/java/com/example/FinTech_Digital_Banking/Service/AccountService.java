package com.example.FinTech_Digital_Banking.Service;

import com.example.FinTech_Digital_Banking.Entity.Account;
import com.example.FinTech_Digital_Banking.Repository.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@Service
public class AccountService {
    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account getAccount(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow();
    }

    public void getBalanceVirtualThreads(Long accountId) {
        var executor = Executors.newVirtualThreadPerTaskExecutor();
        executor.submit(() -> {
            Account account = getAccount(accountId);
            System.out.println("Balance: " + account.getBalance());
        });
        executor.close();
    }
}
