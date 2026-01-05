package com.example.FinTech_Digital_Banking.Service;
import com.example.FinTech_Digital_Banking.DTO.TransferRequest;
import com.example.FinTech_Digital_Banking.Entity.Account;
import com.example.FinTech_Digital_Banking.Entity.LedgerEntry;
import com.example.FinTech_Digital_Banking.Repository.AccountRepository;
import com.example.FinTech_Digital_Banking.Repository.LedgerEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.List;


@Service
public class LedgerService {


private final AccountRepository accountRepository;
private final LedgerEntryRepository ledgerEntryRepository;


public LedgerService(AccountRepository accountRepository,
LedgerEntryRepository ledgerEntryRepository) {
this.accountRepository = accountRepository;
this.ledgerEntryRepository = ledgerEntryRepository;
}


@Transactional(isolation = Isolation.SERIALIZABLE)
public UUID transfer(TransferRequest req, Long performedByUserId) {
if (req.getAmount() == null || req.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
throw new IllegalArgumentException("Amount must be > 0");
}
if (req.getFromAccountId().equals(req.getToAccountId())) {
throw new IllegalArgumentException("from and to accounts must be different");
}


Long a = Math.min(req.getFromAccountId(), req.getToAccountId());
Long b = Math.max(req.getFromAccountId(), req.getToAccountId());


Account first = accountRepository.findByIdForUpdate(a)
.orElseThrow(() -> new IllegalArgumentException("Account not found: " + a));
Account second = accountRepository.findByIdForUpdate(b)
.orElseThrow(() -> new IllegalArgumentException("Account not found: " + b));


Account fromAccount = first.getAccountId().equals(req.getFromAccountId()) ? first : second;
Account toAccount = first.getAccountId().equals(req.getToAccountId()) ? first : second;


if (!fromAccount.getCurrency().equalsIgnoreCase(toAccount.getCurrency())) {
throw new IllegalArgumentException("Currency mismatch between accounts");
}


BigDecimal amount = req.getAmount();


if (fromAccount.getBalance().compareTo(amount) < 0) {
throw new IllegalStateException("Insufficient funds");
}

UUID transactionId=UUID.randomUUID();
LedgerEntry debit=LedgerEntry.builder()
        .transactionId(transactionId)
        .entryTimestamp(OffsetDateTime.now())
        .accountId(fromAccount.getAccountId())
        .relatedAccountId(toAccount.getAccountId())
        .entryType("DEBIT")
        .amount(amount)
        .currency(fromAccount.getCurrency())
        .description(req.getDescription())
        .createdBy(performedByUserId)
        .build();
LedgerEntry credit=LedgerEntry.builder()
        .transactionId(transactionId)
        .entryTimestamp(OffsetDateTime.now())
        .accountId(toAccount.getAccountId())
        .relatedAccountId(fromAccount.getAccountId())
        .entryType("CREDIT")
        .amount(amount)
        .currency(toAccount.getCurrency())
        .description(req.getDescription())
        .createdBy(performedByUserId)
        .build();
ledgerEntryRepository.save(debit);
ledgerEntryRepository.save(credit);

fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
toAccount.setBalance(toAccount.getBalance().add(amount));

accountRepository.save(fromAccount);
accountRepository.save(toAccount);

return transactionId;
}
@Transactional(readOnly=true)
public BigDecimal getBalance(Long accountId)
{
    return accountRepository.findById(accountId)
            .orElseThrow(()-> new IllegalArgumentException("Account not found"))
            .getBalance();
}
@Transactional(isolation = Isolation.SERIALIZABLE)
public void debit(Long accountId, double amount) {

    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be > 0");
    }

    Account account = accountRepository.findByIdForUpdate(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found"));
    System.out.print("account id" +accountId);
    System.out.print(account.getAccountId());
    System.out.print(account.getBalance());
    System.out.print(amount);
            

    BigDecimal debitAmount = BigDecimal.valueOf(amount);

    if (account.getBalance().compareTo(debitAmount) < 0) {
        throw new IllegalStateException("Insufficient balance");
    }

    UUID transactionId = UUID.randomUUID();

    // DEBIT user account
    LedgerEntry debit = LedgerEntry.builder()
            .transactionId(transactionId)
            .entryTimestamp(OffsetDateTime.now())
            .accountId(accountId)
            .entryType("DEBIT")
            .amount(debitAmount)
            .currency(account.getCurrency())
            .description("STOCK_BUY")
            .createdBy(accountId)
            .build();

    // CREDIT system / broker account
    LedgerEntry credit = LedgerEntry.builder()
            .transactionId(transactionId)
            .entryTimestamp(OffsetDateTime.now())
            .accountId(0L) // SYSTEM / BROKER ACCOUNT
            .relatedAccountId(accountId)
            .entryType("CREDIT")
            .amount(debitAmount)
            .currency(account.getCurrency())
            .description("STOCK_BUY")
            .createdBy(accountId)
            .build();

    ledgerEntryRepository.save(debit);
    ledgerEntryRepository.save(credit);

    // Update balance
    account.setBalance(account.getBalance().subtract(debitAmount));
    accountRepository.save(account);
}

@Transactional(isolation = Isolation.SERIALIZABLE)
public void credit(Long accountId, double amount) {

    if (amount <= 0) {
        throw new IllegalArgumentException("Amount must be > 0");
    }

    Account account = accountRepository.findByIdForUpdate(accountId)
            .orElseThrow(() -> new IllegalArgumentException("Account not found"));

    BigDecimal creditAmount = BigDecimal.valueOf(amount);

    UUID transactionId = UUID.randomUUID();

    // CREDIT user account
    LedgerEntry credit = LedgerEntry.builder()
            .transactionId(transactionId)
            .entryTimestamp(OffsetDateTime.now())
            .accountId(accountId)
            .entryType("CREDIT")
            .amount(creditAmount)
            .currency(account.getCurrency())
            .description("STOCK_SELL")
            .createdBy(accountId)
            .build();

    // DEBIT system / broker account
    LedgerEntry debit = LedgerEntry.builder()
            .transactionId(transactionId)
            .entryTimestamp(OffsetDateTime.now())
            .accountId(0L) // SYSTEM / BROKER
            .relatedAccountId(accountId)
            .entryType("DEBIT")
            .amount(creditAmount)
            .currency(account.getCurrency())
            .description("STOCK_SELL")
            .createdBy(accountId)
            .build();

    ledgerEntryRepository.save(debit);
    ledgerEntryRepository.save(credit);

    // Update balance
    account.setBalance(account.getBalance().add(creditAmount));
    accountRepository.save(account);
}

}