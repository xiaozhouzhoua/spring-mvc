package com.spring.springmvc.service;

import com.spring.springmvc.dao.StockDao;
import com.spring.springmvc.model.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StockServiceImpl implements StockService {
    @Autowired
    private StockDao stockDao;

    @Override
    public List<Stock> getAllStocks() {
        return stockDao.findAll().stream().map(stockDO -> Stock.builder()
                .symbol(stockDO.getSymbol())
                .name(stockDO.getName())
                .build()
        ).collect(Collectors.toList());
    }
}
