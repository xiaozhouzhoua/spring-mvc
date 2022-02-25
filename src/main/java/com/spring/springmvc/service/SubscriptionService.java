package com.spring.springmvc.service;

import com.spring.springmvc.model.StockSubscription;

import java.util.List;

public interface SubscriptionService {
    List<StockSubscription> findByEmail(String email);

    void addSubscription(String email, String symbol);
}
