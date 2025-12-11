package com.example.FinTech_Digital_Banking.Controller;

import com.example.FinTech_Digital_Banking.DTO.TransferRequest;
import com.example.FinTech_Digital_Banking.DTO.TransferResponse;
import com.example.FinTech_Digital_Banking.Service.TransferService;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
@CrossOrigin(origins = "http://localhost:3001")
@RestController
@RequestMapping("/api")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransferResponse> transfer(@RequestBody TransferRequest req,
                                                     @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        Long performedBy = userId == null ? -1L : userId;
        UUID txId = transferService.transfer(req, performedBy);
        return ResponseEntity.ok(TransferResponse.builder()
                .transactionId(txId.toString())
                .status("OK")
                .message("Transfer completed")
                .build());
    }

  
    @PostMapping("/debug/transfer/simulate")
    public ResponseEntity<String> simulateConcurrentTransfer(@RequestBody TransferRequest req,
                                                             @RequestParam(defaultValue = "100") int count,
                                                             @RequestHeader(value = "X-User-Id", required = false) Long userId) throws InterruptedException {
        Long performedBy = userId == null ? -1L : userId;

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < count; i++) {
                executor.submit(() -> {
                    try {
                        transferService.transfer(req, performedBy);
                    } catch (Exception ex) {
                        // keep silent for simulation (insufficient funds etc.)
                    }
                });
            }
            executor.shutdown();
            boolean finished = executor.awaitTermination(60, TimeUnit.SECONDS);
            return ResponseEntity.ok("Submitted " + count + " transfers. Finished: " + finished);
        }
    }
}

