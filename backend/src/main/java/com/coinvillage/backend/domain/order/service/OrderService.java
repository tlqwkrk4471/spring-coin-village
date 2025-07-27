package com.coinvillage.backend.domain.order.service;

import com.coinvillage.backend.domain.coin.Coin;
import com.coinvillage.backend.domain.order.Order;
import com.coinvillage.backend.domain.user.User;

import java.util.List;
import java.util.UUID;

public interface OrderService {

    Boolean placeOrder(Order order);
    Boolean cancelOrder(UUID id);
    List<Order> getActiveOrdersByUser(User user);
    List<Order> getActiveOrdersByCoin(Coin coin);
}
