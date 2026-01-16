package com.example.FinTech_Digital_Banking.Controller;

import com.example.FinTech_Digital_Banking.DTO.TransferRequest;
import com.example.FinTech_Digital_Banking.DTO.TransferResponse;
import com.example.FinTech_Digital_Banking.Service.OtpTransferService;
import com.example.FinTech_Digital_Banking.Configuration.JwtServices;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.FinTech_Digital_Banking.DTO.OtpConfirmRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.FinTech_Digital_Banking.Service.TransferService;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api")

public class TransferController {
    

 
    private final OtpTransferService otpTransferService;
    private final TransferService transferService;

    public TransferController(OtpTransferService otpTransferService,TransferService transferService,
                              JwtServices jwtServices) {
        this.otpTransferService = otpTransferService;
        this.transferService=transferService;
    }

    // =========================
    // 1️⃣ Initiate Transfer
    // =========================

 @PostMapping("/transfer")
public ResponseEntity<TransferResponse> transfer(
        @RequestBody TransferRequest req) {

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated()) {
        return ResponseEntity.status(401).build();
    }

    String email = auth.getName(); // comes from JWT subject

    UUID txId = otpTransferService.initiateTransfer(req, email);

    boolean otpRequired =
            req.getAmount().compareTo(otpTransferService.getThreshold()) > 0;

    return ResponseEntity.ok(
            TransferResponse.builder()
                    .transactionId(txId.toString())
                    .status(otpRequired ? "PENDING" : "COMPLETED")
                    .otpRequired(otpRequired)
                    .message(
                            otpRequired
                                    ? "OTP sent to registered email"
                                    : "Transfer completed successfully"
                    )
                    .build()
    );
}




    // =========================
    // 2️⃣ Confirm OTP
    // =========================
@PostMapping("/transfer/confirm-otp")

public ResponseEntity<TransferResponse> confirmOtp(
        @RequestBody OtpConfirmRequest request) {

    UUID finalTxId = otpTransferService.confirmOtp(
            UUID.fromString(request.getTransactionId()),
            request.getOtp()
    );

    return ResponseEntity.ok(
            TransferResponse.builder()
                    .transactionId(finalTxId.toString())
                    .status("OK")
                    .otpRequired(false)
                    .message("Transfer completed after OTP verification")
                    .build()
    );
}


    // =========================
    // 3️⃣ Debug: Simulate concurrent transfers
    // =========================
    @PostMapping("/debug/transfer/simulate")
    public ResponseEntity<String> simulateConcurrentTransfer(
            @RequestBody TransferRequest req,
            @RequestParam(defaultValue = "100") int count) throws InterruptedException {

//        Long performedBy = userId == null ? -1L : userId;
Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    if (auth == null || !auth.isAuthenticated()) {
        return ResponseEntity.status(401).build();
    }

    String email = auth.getName(); 


        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < count; i++) {
                executor.submit(() -> {
                    try {
                        otpTransferService.initiateTransfer(req,email);
                    } catch (Exception ex) {
                        // ignore errors in simulation (e.g., insufficient funds)
                    }
                });
            }
            executor.shutdown();
            boolean finished = executor.awaitTermination(60, TimeUnit.SECONDS);
            return ResponseEntity.ok("Submitted " + count + " transfers. Finished: " + finished);
        }
    }
    
    
    
    
    
    
    
    @PostMapping("/debug/transfer/simulate-direct")
    public ResponseEntity<String> simulateDirectConcurrentTransfer(
            @RequestBody TransferRequest req,
            @RequestParam(defaultValue = "100") int count) throws InterruptedException {

        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (int i = 0; i < count; i++) {
                executor.submit(() -> {
                    try {
                        // Directly call TransferService.transfer to bypass OTP
                        transferService.transfer(req, -1L);
                    } catch (Exception ignored) {
                        // Insufficient funds or other errors expected in some threads
                    }
                });
            }
            executor.shutdown();
            boolean finished = executor.awaitTermination(60, TimeUnit.SECONDS);

            return ResponseEntity.ok(
                    "Submitted " + count + " direct transfers. Finished: " + finished
            );
        }
    }
}
