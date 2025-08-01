package com.coinvillage.backend.domain.trade.service;

import com.coinvillage.backend.domain.order.*;
import com.coinvillage.backend.domain.order.entry.Order;
import com.coinvillage.backend.domain.order.entry.OrderBook;
import com.coinvillage.backend.domain.order.entry.OrderType;
import com.coinvillage.backend.domain.trade.Trade;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMatchingEngine {

    private final OrderBookManager orderBookManager;

    public List<Trade> match(Order order) {
        List<Trade> trades = new ArrayList<>();
        OrderBook orderBook = orderBookManager.getOrderBook(order.getCoin());

        while (!orderBook.getBuyOrders().isEmpty() && !orderBook.getSellOrders().isEmpty()) {
            Order buyOrder = orderBook.peekBuyOrder();
            Order sellOrder = orderBook.peekSellOrder();

            boolean priceMatch = buyOrder.getOrderType() == OrderType.MARKET ||
                                sellOrder.getOrderType() == OrderType.MARKET ||
                                buyOrder.getPrice() >= sellOrder.getPrice();

            if (!priceMatch) break;

            long tradeQuantity = Math.min(buyOrder.getQuantity() - buyOrder.getFilledQuantity(),
                                sellOrder.getQuantity() - sellOrder.getFilledQuantity());
            int tradePrice = determineTradePrice(buyOrder, sellOrder);

            Trade trade = new Trade(buyOrder, sellOrder, tradePrice, tradeQuantity);
            trades.add(trade);

            buyOrder.setFilledQuantity(buyOrder.getFilledQuantity() + tradeQuantity);
            sellOrder.setFilledQuantity(sellOrder.getFilledQuantity() + tradeQuantity);

            if (buyOrder.getFilledQuantity() == buyOrder.getQuantity()) {
                orderBook.pollBuyOrder();
            }
            if (sellOrder.getFilledQuantity() == sellOrder.getQuantity()) {
                orderBook.pollSellOrder();
            }
        }

        return trades;
    }

    private int determineTradePrice(Order buy, Order sell) {
        if (buy.getOrderType() == OrderType.MARKET && sell.getOrderType() == OrderType.MARKET) {
            return buy.getCoin().getCurrentPrice();
        } else if (buy.getOrderType() == OrderType.MARKET) {
            return sell.getPrice();
        } else if (sell.getOrderType() == OrderType.MARKET) {
            return buy.getPrice();
        } else {
            return buy.getCreateAt().isBefore(sell.getCreateAt()) ? buy.getPrice() : sell.getPrice();
        }
    }
}
