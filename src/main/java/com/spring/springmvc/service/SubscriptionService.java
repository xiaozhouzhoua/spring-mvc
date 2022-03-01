package com.spring.springmvc.service;

import com.spring.springmvc.model.StockSubscription;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface SubscriptionService {
    List<StockSubscription> findByEmail(String email);

    void addSubscription(String email, String symbol);

    Future<List<StockSubscription>> findByEmailAsync(String email);

    Future<Void> addSubscriptionAsync(String email, String symbol);

    CompletableFuture<List<StockSubscription>> findByEmailByCf(String email);

    CompletableFuture<Void> addSubscriptionByCf(String email, String symbol);

    Flux<StockSubscription> findByEmailReactor(String email);
}
