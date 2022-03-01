package com.spring.springmvc.service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface StockPriceService {
    Map<String, String> getPrice(String user);

    Future<Map<String, String>> getPriceAsync(String user);

    CompletableFuture<Map<String, String>> getPriceByCf(String user);
}
