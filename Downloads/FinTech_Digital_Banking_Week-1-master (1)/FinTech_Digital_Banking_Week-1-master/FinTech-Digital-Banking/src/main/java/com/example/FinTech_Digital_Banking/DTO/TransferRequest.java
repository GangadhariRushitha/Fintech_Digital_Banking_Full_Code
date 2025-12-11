// TransferRequest.java
package com.example.FinTech_Digital_Banking.DTO;



import lombok.*;


import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {
private Long fromAccountId;
private Long toAccountId;
private BigDecimal amount;
private String currency;
private String description;
}
