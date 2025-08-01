package com.coinvillage.backend.domain.trade.service;

import com.coinvillage.backend.domain.order.entry.Order;

public interface TradeService {

    void processOrder(Order order);
}
