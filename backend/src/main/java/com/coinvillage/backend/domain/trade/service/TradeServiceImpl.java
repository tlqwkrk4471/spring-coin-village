package com.coinvillage.backend.domain.trade.service;

import com.coinvillage.backend.domain.coin.entry.Coin;
import com.coinvillage.backend.domain.coin.entry.Wallet;
import com.coinvillage.backend.domain.order.entry.Order;
import com.coinvillage.backend.domain.order.entry.OrderStatus;
import com.coinvillage.backend.domain.trade.Trade;
import com.coinvillage.backend.domain.trade.repository.TradeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService{

    private final OrderMatchingEngine orderMatchingEngine;
    private final TradeRepository tradeRepository;

    public void processOrder(Order order) {
        List<Trade> trades = orderMatchingEngine.match(order);

        for (Trade trade : trades) {
            Order buyOrder = trade.getBuyOrder();
            Order sellOrder = trade.getSellOrder();

            Wallet buyerWallet = buyOrder.getUser().getWallet();
            Wallet sellerWallet = sellOrder.getUser().getWallet();
            Coin coin = trade.getCoin();
            int price = trade.getPrice();
            long quantity = trade.getQuantity();
            long totalAmount = price * quantity;

            if (buyOrder.getFilledQuantity() == buyOrder.getQuantity()) {
                buyOrder.setOrderStatus(OrderStatus.FILLED);
            } else {
                buyOrder.setOrderStatus(OrderStatus.PARTIAL_FILLED);
            }

            if (sellOrder.getFilledQuantity() == sellOrder.getQuantity()) {
                sellOrder.setOrderStatus(OrderStatus.FILLED);
            } else {
                sellOrder.setOrderStatus(OrderStatus.PARTIAL_FILLED);
            }

            coin.setCurrentPrice(price);

            buyerWallet.substractCash(totalAmount);
            buyerWallet.addCoin(coin, quantity);

            sellerWallet.addCash(totalAmount);
            sellerWallet.substractCoin(coin, quantity);

            tradeRepository.save(trade);
        }
    }
}
