package com.example.FinTech_Digital_Banking.Service;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.FinTech_Digital_Banking.Repository.PortfolioRepository;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class PortfolioService {

    private final PortfolioRepository repository;
    private final StockPriceService stockPriceService;

    public List<Map<String, Object>> getPortfolio() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return repository.findByUserId(email).stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    BigDecimal price = stockPriceService.fetchStockPrice(p.getStockSymbol());

                    map.put("symbol", p.getStockSymbol());
                    map.put("quantity", p.getQuantity());
                    map.put("currentPrice", price);
                    map.put("totalValue", price.multiply(BigDecimal.valueOf(p.getQuantity())));
                    return map;
                }).toList();
    }
}
