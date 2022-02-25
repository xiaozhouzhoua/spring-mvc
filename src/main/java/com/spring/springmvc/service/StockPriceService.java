package com.spring.springmvc.service;

import java.util.Map;

public interface StockPriceService {
    Map<String, String> getPrice(String user);
}
