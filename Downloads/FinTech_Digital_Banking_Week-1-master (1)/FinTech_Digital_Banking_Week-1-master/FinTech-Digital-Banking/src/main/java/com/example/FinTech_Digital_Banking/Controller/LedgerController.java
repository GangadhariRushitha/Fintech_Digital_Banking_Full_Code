package com.example.FinTech_Digital_Banking.Controller;

import com.example.FinTech_Digital_Banking.Entity.Account;
import com.example.FinTech_Digital_Banking.Entity.LedgerEntry;
import com.example.FinTech_Digital_Banking.DTO.TransferRequest;
import com.example.FinTech_Digital_Banking.DTO.TransferResponse;
import com.example.FinTech_Digital_Banking.Service.LedgerService;
import com.example.FinTech_Digital_Banking.Service.AccountService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.List;

@RestController
@RequestMapping("/api")
public class LedgerController {

    private final LedgerService ledgerService;


    public LedgerController(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
       
    }
@PostMapping("/ledger/transfer")
public ResponseEntity<TransferResponse>transfer(@RequestBody TransferRequest req,
            @RequestHeader(value="X-User-Id",required=false)Long userId)
{
    Long performedBy= userId==null? -1L:userId;
    UUID txId=ledgerService.transfer(req, performedBy);
    return ResponseEntity.ok(TransferResponse.builder()
    .transactionId(txId.toString())
    .status("OK")
    .message("Transfer compelted")
    .build());
    
    
}
    @GetMapping("/accounts/{id}/balance")
    public ResponseEntity<BigDecimal>getBalance(@PathVariable Long id)
    {
        BigDecimal balance=ledgerService.getBalance(id);
        return ResponseEntity.ok().body(balance);
    }
    @PostMapping("/debug/balance/simulate")
    public ResponseEntity<String> simulateBalanceConcurrent(@RequestParam Long accountId,
            @RequestParam int count) throws InterruptedException{
        if(count<=0) count=1000;
        
        try(ExecutorService executor=Executors.newVirtualThreadPerTaskExecutor()){
            for(int i=0;i<count;i++)
            {
                executor.submit(()->{
               try{
                   ledgerService.getBalance(accountId);
               }
               catch(Exception ex)
               {
                   
               }
                });
            }
            executor.shutdown();
            boolean finished=executor.awaitTermination(30, TimeUnit.SECONDS);
            return ResponseEntity.ok().body("Submitteed"+count+"virtual threads finised"+finished);
        }
    }
   

}












