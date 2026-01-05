/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.example.FinTech_Digital_Banking.Controller;


import com.example.FinTech_Digital_Banking.Service.PortfolioService;
import java.util.*;
import org.springframework.web.bind.annotation.*;


import org.springframework.security.core.Authentication;


 

@RestController
@RequestMapping("/portfolio")

public class PortfolioController {

    private final PortfolioService portfolioService;
       public PortfolioController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping
    public List<Map<String, Object>> getPortfolio() {
        return portfolioService.getPortfolio();
    }
}
