package com.coinvillage.backend.domain.order.service;

import com.coinvillage.backend.domain.coin.entry.Coin;
import com.coinvillage.backend.domain.order.entry.Order;
import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.order.dto.OrderResponseByCoin;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Boolean placeOrder(Order order);
    Boolean cancelOrder(UUID id);
    List<Order> getActiveOrdersByUser(User user);
    List<Order> getActiveOrdersByCoin(Coin coin);
    List<OrderResponseByCoin> getAggregatedActiveOrdersByCoin(Coin coin);
    List<Order> getFilledOrdersByUser(User user);
    List<Order> getFilledOrdersByCoin(Coin coin);
}
