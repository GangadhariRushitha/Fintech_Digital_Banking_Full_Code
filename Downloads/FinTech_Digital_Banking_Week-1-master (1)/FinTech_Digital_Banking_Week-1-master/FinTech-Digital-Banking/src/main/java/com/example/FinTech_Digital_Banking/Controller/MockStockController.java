/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.FinTech_Digital_Banking.Controller;

import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
@RestController
@RequestMapping("/mock/stock")
public class MockStockController {

    @GetMapping("/{symbol}")
    public Map<String, Object> getStock(@PathVariable String symbol) {
        // simple hardcoded prices
        Map<String, Object> prices = Map.of(
                "AAPL", 134.22,
                "GOOGL", 1520.50,
                "TSLA", 810.00
        );

        return Map.of(
                "symbol", symbol,
                "price", prices.getOrDefault(symbol, 100.0)
        );
    }
}

