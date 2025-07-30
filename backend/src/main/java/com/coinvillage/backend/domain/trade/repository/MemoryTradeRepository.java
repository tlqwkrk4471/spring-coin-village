package com.coinvillage.backend.domain.trade.repository;

import com.coinvillage.backend.domain.trade.Trade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Repository
public class MemoryTradeRepository implements TradeRepository {

    private final Map<UUID, Trade> store = new ConcurrentHashMap<>();

    public Trade save(Trade trade) {
        store.put(trade.getTradeId(), trade);
        log.info("save: trade={}", trade);
        return trade;
    }

    public Optional<Trade> findById(UUID tradeId) {
        return Optional.ofNullable(store.get(tradeId));
    }

    public List<Trade> findByUserId(Long userId) {
        List<Trade> result = new ArrayList<>();
        for (Trade trade : store.values()) {
            if (trade.getBuyOrder().getUser().getId().equals(userId) ||
                    trade.getSellOrder().getUser().getId().equals(userId)) {
                result.add(trade);
            }
        }
        return result;
    }

    public List<Trade> findByCoinSymbol(String symbol) {
        List<Trade> result = new ArrayList<>();
        for (Trade trade : store.values()) {
            if (trade.getCoin().getSymbol().equalsIgnoreCase(symbol)) {
                result.add(trade);
            }
        }
        return result;
    }

    public List<Trade> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clear() {
        store.clear();
    }
}
