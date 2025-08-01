package com.coinvillage.backend.domain.order.service;

import com.coinvillage.backend.domain.coin.entry.Coin;
import com.coinvillage.backend.domain.order.*;
import com.coinvillage.backend.domain.order.entry.Order;
import com.coinvillage.backend.domain.order.entry.OrderSide;
import com.coinvillage.backend.domain.order.entry.OrderStatus;
import com.coinvillage.backend.domain.order.entry.OrderType;
import com.coinvillage.backend.domain.order.repository.OrderRepository;
import com.coinvillage.backend.domain.trade.service.TradeService;
import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.order.dto.OrderResponseByCoin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderBookManager orderBookManager;
    private final TradeService tradeService;

    @Override
    public Boolean placeOrder(Order order) {
        if (order.getOrderType() == OrderType.LIMIT) {
            Long cashBalance = order.getUser().getWallet().getCashBalance();
            long totalPrice = order.getPrice() * order.getQuantity();

            if (order.getOrderSide() == OrderSide.BUY) {
                if (cashBalance - totalPrice >= 0) {
                    orderRepository.save(order);
                    orderBookManager.getOrderBook(order.getCoin()).addOrder(order);
                    tradeService.processOrder(order);
                } else {
                    return false;
                }
            } else if (order.getOrderSide() == OrderSide.SELL) {
                orderRepository.save(order);
                orderBookManager.getOrderBook(order.getCoin()).addOrder(order);
                tradeService.processOrder(order);
            }
        } else {
            orderRepository.save(order);
            orderBookManager.getOrderBook(order.getCoin()).addOrder(order);
            tradeService.processOrder(order);
        }

        return true;
    }

    @Override
    public Boolean cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).get();
        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.update(order);
        orderBookManager.getOrderBook(order.getCoin()).removeOrder(order);
        log.info("cancel: order={}", order);
        return true;
    }

    @Override
    public List<Order> getActiveOrdersByUser(User user) {
        return orderRepository.findActiveOrdersByUser(user);
    }

    @Override
    public List<Order> getActiveOrdersByCoin(Coin coin) {
        return orderRepository.findActiveOrdersByCoin(coin);
    }

    @Override
    public List<OrderResponseByCoin> getAggregatedActiveOrdersByCoin(Coin coin) {
        List<Order> activeOrders = getActiveOrdersByCoin(coin);
        Map<Integer, OrderResponseByCoin> priceMap = new TreeMap<>();

        for (Order order : activeOrders) {
            if (order.getOrderType() == OrderType.MARKET) continue;

            int price = order.getPrice();
            long quantity = order.getQuantity();

            OrderResponseByCoin entry = priceMap.getOrDefault(price, new OrderResponseByCoin(price, 0, 0));

            if (order.getOrderSide() == OrderSide.BUY) {
                entry.setBuyQuantity(entry.getBuyQuantity() + quantity);
            } else {
                entry.setSellQuantity(entry.getSellQuantity() + quantity);
            }

            priceMap.put(price, entry);
        }

        return new ArrayList<>(priceMap.values());
    }

    @Override
    public List<Order> getFilledOrdersByUser(User user) {
        return orderRepository.findFilledOrdersByUser(user);
    }

    @Override
    public List<Order> getFilledOrdersByCoin(Coin coin) {
        return orderRepository.findFilledOrdersByCoin(coin);
    }
}
