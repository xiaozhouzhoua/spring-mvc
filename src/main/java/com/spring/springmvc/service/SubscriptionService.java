package com.spring.springmvc.service;

import com.spring.springmvc.model.StockSubscription;

import java.util.List;
import java.util.concurrent.Future;

public interface SubscriptionService {
    List<StockSubscription> findByEmail(String email);

    void addSubscription(String email, String symbol);

    Future<List<StockSubscription>> findByEmailAsync(String email);

    Future<Void> addSubscriptionAsync(String email, String symbol);
}
