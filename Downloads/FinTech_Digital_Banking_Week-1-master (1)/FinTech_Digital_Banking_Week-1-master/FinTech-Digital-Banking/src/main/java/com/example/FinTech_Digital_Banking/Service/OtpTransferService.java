package com.example.FinTech_Digital_Banking.Service;

import com.example.FinTech_Digital_Banking.DTO.TransferRequest;
import com.example.FinTech_Digital_Banking.Entity.PendingTransaction;
import com.example.FinTech_Digital_Banking.Repository.PendingTransactionRepository;
import com.example.FinTech_Digital_Banking.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class OtpTransferService {

    private final TransferService transferService;
    private final PendingTransactionRepository pendingRepo;
    private final UserRepository userRepository;
    private final EmailService emailService;
  
//
    @Value("${transfer.otp.threshold}")
   private BigDecimal threshold;

    public OtpTransferService(
            TransferService transferService,
            PendingTransactionRepository pendingRepo,
            UserRepository userRepository,
            EmailService emailService) {

        this.transferService = transferService;
        this.pendingRepo = pendingRepo;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }
    
    public BigDecimal getThreshold() {
    return threshold;
}
//
//
//    // 🔹 STEP 1: Initiate Transfer
    public UUID initiateTransfer(TransferRequest req, String email) {
        
        Long userId = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("User not found"))
        .getId();
//
//          
//        // BELOW threshold → direct transfer
        if (req.getAmount().compareTo(threshold) <= 0) {
            return transferService.transfer(req, userId);
       }

//        // ABOVE threshold → OTP required
        UUID txId = UUID.randomUUID();
        String otp = String.valueOf(100000 + new SecureRandom().nextInt(900000));

        PendingTransaction pt = PendingTransaction.builder()
                .transactionId(txId)
                .fromAccountId(req.getFromAccountId())
                .toAccountId(req.getToAccountId())
                .amount(req.getAmount())
                .performedByUserId(userId)
                .otp(otp)
                .expiryTime(OffsetDateTime.now().plusMinutes(5))
                .verified(false)
                .build();

        pendingRepo.save(pt);

//        email = userRepository.findById(userId)
//                .orElseThrow()
//                .getEmail();
        emailService.sendOtp(email, otp);
//System.out.println("Generated OTP for testing: " + otp);

        return txId;
    }
    

//    // 🔹 STEP 2: Confirm OTP
    @Transactional
    public UUID confirmOtp(UUID transactionId, String otp) {

        PendingTransaction pt = pendingRepo
                .findByTransactionIdAndOtpAndVerifiedFalse(transactionId, otp)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

       if (pt.getExpiryTime().isBefore(OffsetDateTime.now())) {
            throw new RuntimeException("OTP expired");
        }
          if (!pt.getOtp().equals(otp)) {
        throw new RuntimeException("Invalid OTP");
    }

        pt.setVerified(true);
        pendingRepo.save(pt);

       
    TransferRequest req = new TransferRequest();
    req.setFromAccountId(pt.getFromAccountId());
    req.setToAccountId(pt.getToAccountId());
    req.setAmount(pt.getAmount());
    req.setDescription("OTP verified transfer");

        return transferService.transfer(req, pt.getPerformedByUserId());
    }
}

