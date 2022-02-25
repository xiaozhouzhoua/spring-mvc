package com.spring.springmvc.controller;

import com.spring.springmvc.model.Stock;
import com.spring.springmvc.model.StockSymbol;
import com.spring.springmvc.service.StockServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/stocks")
public class StockController {
    @Autowired
    private StockServiceImpl stockService;

    @GetMapping
    private String getStocks(Model model) {
        List<Stock> stocks = stockService.getAllStocks();
        model.addAttribute("stocks", stocks);
        model.addAttribute("stockSymbol", new StockSymbol());
        return "stocks";
    }
}
