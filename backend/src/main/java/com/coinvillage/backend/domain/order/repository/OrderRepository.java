package com.coinvillage.backend.domain.order.repository;

import com.coinvillage.backend.domain.coin.Coin;
import com.coinvillage.backend.domain.order.Order;
import com.coinvillage.backend.domain.user.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface OrderRepository {

    Order save(Order order);
    Optional<Order> findById(UUID id);
    List<Order> findByUser(User user);
    List<Order> findByCoin(Coin coin);
    List<Order> findAll();
    List<Order> findActiveOrdersByCoin(Coin coin);
    List<Order> findActiveOrdersByUser(User user);
    void update(Order order);
    void delete(Order order);
    void clear();
}
