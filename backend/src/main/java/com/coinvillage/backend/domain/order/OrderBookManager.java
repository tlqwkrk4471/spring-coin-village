package com.coinvillage.backend.domain.order;

import com.coinvillage.backend.domain.coin.entry.Coin;
import com.coinvillage.backend.domain.order.entry.OrderBook;
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
