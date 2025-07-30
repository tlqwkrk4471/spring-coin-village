package com.coinvillage.backend.domain.order;

import com.coinvillage.backend.domain.coin.Coin;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class OrderBookManager {

    private final Map<String, OrderBook> orderBookMap = new ConcurrentHashMap<>();

    public OrderBook getOrderBook(Coin coin) {
        return orderBookMap.computeIfAbsent(coin.getSymbol(), s -> new OrderBook());
    }
}
