package com.coinvillage.backend.domain.trade.repository;

import com.coinvillage.backend.domain.trade.Trade;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TradeRepository {

    Trade save(Trade trade);
    Optional<Trade> findById(UUID tradeId);
    List<Trade> findByUserId(Long userId);
    List<Trade> findByCoinSymbol(String symbol);
    List<Trade> findAll();
    void clear();
}
