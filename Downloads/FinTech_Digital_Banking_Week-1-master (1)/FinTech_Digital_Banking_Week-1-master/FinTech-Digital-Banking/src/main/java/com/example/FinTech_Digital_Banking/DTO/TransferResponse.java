
package com.example.FinTech_Digital_Banking.DTO;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferResponse {
private String transactionId;
private String status;
private String message;
private boolean otpRequired;
        }