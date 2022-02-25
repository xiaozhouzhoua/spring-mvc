package com.spring.springmvc.dao;

import com.spring.springmvc.entity.StockSubscriptionDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockSubscriptionDao extends JpaRepository<StockSubscriptionDO, Long> {
    List<StockSubscriptionDO> findByEmail(String email);

    List<StockSubscriptionDO> findByEmailAndSymbol(String email, String symbol);
}
