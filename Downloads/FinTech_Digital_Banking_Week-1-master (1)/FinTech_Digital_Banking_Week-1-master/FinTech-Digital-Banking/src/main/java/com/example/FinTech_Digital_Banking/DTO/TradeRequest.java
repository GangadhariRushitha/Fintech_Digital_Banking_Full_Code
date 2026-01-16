
package com.example.FinTech_Digital_Banking.DTO;


public record TradeRequest(
        Long accountId,
        String symbol,
        int quantity
) {}

