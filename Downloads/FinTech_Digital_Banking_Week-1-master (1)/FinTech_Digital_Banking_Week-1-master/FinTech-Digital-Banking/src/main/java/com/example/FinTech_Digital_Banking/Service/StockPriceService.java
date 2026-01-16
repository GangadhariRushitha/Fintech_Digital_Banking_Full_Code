/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.FinTech_Digital_Banking.Service;

/**
 *
 * @author admin
 */
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.math.BigDecimal;

@Service
public class StockPriceService {

    private final RestTemplate restTemplate = new RestTemplate();

    public BigDecimal fetchStockPrice(String symbol) {
    try {
        long start = System.currentTimeMillis();

        Map response = restTemplate.getForObject(
                "http://localhost:9099/mock/stock/" + symbol,
                Map.class
        );

        long latency = System.currentTimeMillis() - start;
        System.out.println("Stock API latency: " + latency + " ms");

        if (response != null && response.get("price") != null) {
            return new BigDecimal(response.get("price").toString());
        } else {
            System.out.println("Mock API returned null, using fallback price");
            return BigDecimal.valueOf(100); // fallback price
        }
    } catch (Exception e) {
        System.err.println("Failed to fetch stock price: " + e.getMessage());
        return BigDecimal.valueOf(100); // fallback price
    }
}

}
