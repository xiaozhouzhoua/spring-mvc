package com.spring.springmvc.service;

import com.spring.springmvc.dao.StockSubscriptionDao;
import com.spring.springmvc.entity.StockSubscriptionDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class StockPriceServiceImpl implements StockPriceService {
    @Autowired
    private StockSubscriptionDao subscriptionDao;

    @Autowired
    private PriceQueryEngine priceQueryEngine;

    @Override
    public Map<String, String> getPrice(String email) {
        List<StockSubscriptionDO> subscriptions = subscriptionDao.findByEmail(email);
        Map<String, String> priceMap = subscriptions.stream()
                .map(subscription -> subscription.getSymbol())
                .collect(Collectors.toMap(
                                Function.identity(),
                                priceQueryEngine::getPriceForSymbol
                        )
                );
        return priceMap;
    }

    @Async
    @Override
    public Future<Map<String, String>> getPriceAsync(String user) {
        return new AsyncResult<>(getPrice(user));
    }

    @Override
    public CompletableFuture<Map<String, String>> getPriceByCf(String user) {
        return CompletableFuture.supplyAsync(() -> getPrice(user));
    }
}
