package com.spring.springmvc.service;

import com.spring.springmvc.dao.StockDao;
import com.spring.springmvc.dao.StockSubscriptionDao;
import com.spring.springmvc.entity.StockDO;
import com.spring.springmvc.entity.StockSubscriptionDO;
import com.spring.springmvc.model.StockSubscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService{
    @Autowired
    private StockDao stockDao;

    @Autowired
    private StockSubscriptionDao subscriptionDao;

    public List<StockSubscription> findByEmail(String email) {
        List<StockSubscriptionDO> subscriptions = subscriptionDao.findByEmail(email);
        return subscriptions.stream().map(stockSubscriptionDO ->
                StockSubscription.builder()
                        .symbol(stockSubscriptionDO.getSymbol())
                        .email(stockSubscriptionDO.getEmail())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public void addSubscription(String email, String symbol) {
        checkValidStock(symbol);
        checkSubscriptionExists(email, symbol);
        StockSubscriptionDO subscriptionDO = new StockSubscriptionDO();
        subscriptionDO.setEmail(email);
        subscriptionDO.setSymbol(symbol);
        subscriptionDao.save(subscriptionDO);
    }

    private void checkSubscriptionExists(String email, String symbol) {
        List<StockSubscriptionDO> matched = subscriptionDao.findByEmailAndSymbol(email, symbol);
        if (!CollectionUtils.isEmpty(matched)) {
            throw new RuntimeException("subscription already exists for this user");
        }
    }

    private void checkValidStock(String symbol) {
        Optional<StockDO> matched = stockDao.findAll()
                .stream()
                .filter(stockDO -> stockDO.getSymbol().equalsIgnoreCase(symbol))
                .findAny();
        if (!matched.isPresent()) {
            throw new RuntimeException("stock symbol not valid");
        }
    }

    @Async
    @Override
    public Future<List<StockSubscription>> findByEmailAsync(String email) {
        return new AsyncResult<>(findByEmail(email));
    }

    @Async
    @Override
    public Future<Void> addSubscriptionAsync(String email, String symbol) {
        addSubscription(email, symbol);
        return new AsyncResult<>(null);
    }

    @Override
    public CompletableFuture<Void> addSubscriptionByCf(String email, String symbol) {
        return CompletableFuture.runAsync(() -> addSubscription(email, symbol));
    }

    @Override
    public CompletableFuture<List<StockSubscription>> findByEmailByCf(String email) {
        return CompletableFuture.supplyAsync(() -> findByEmail(email));
    }

    @Override
    public Flux<StockSubscription> findByEmailReactor(String email) {
        return Flux.fromIterable(findByEmail(email));
    }
}
