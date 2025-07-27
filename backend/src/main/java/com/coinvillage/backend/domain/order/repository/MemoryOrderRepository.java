package com.coinvillage.backend.domain.order.repository;

import com.coinvillage.backend.domain.coin.Coin;
import com.coinvillage.backend.domain.order.Order;
import com.coinvillage.backend.domain.order.OrderStatus;
import com.coinvillage.backend.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class MemoryOrderRepository implements OrderRepository {

    private final Map<UUID, Order> store = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        store.put(order.getId(), order);
        log.info("save: order={}", order);
        return order;
    }

    @Override
    public Optional<Order> findById(UUID id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public List<Order> findByUser(User user) {
        return store.values().stream()
                .filter(order -> order.getUser().equals(user))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findByCoin(Coin coin) {
        return store.values().stream()
                .filter(order -> order.getCoin().equals(coin))
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public List<Order> findActiveOrdersByCoin(Coin coin) {
        return store.values().stream()
                .filter(order -> order.getCoin().equals(coin))
                .filter(order -> order.getOrderStatus() == OrderStatus.PENDING
                        || order.getOrderStatus() == OrderStatus.PARTIAL)
                .collect(Collectors.toList());
    }

    @Override
    public List<Order> findActiveOrdersByUser(User user) {
        return store.values().stream()
                .filter(order -> order.getUser().equals(user))
                .filter(order -> order.getOrderStatus() == OrderStatus.PENDING
                        || order.getOrderStatus() == OrderStatus.PARTIAL)
                .collect(Collectors.toList());
    }

    @Override
    public void update(Order order) {
        store.put(order.getId(), order);
    }

    @Override
    public void delete(Order order) {
        store.remove(order.getId());
    }

    @Override
    public void clear() {
        store.clear();
    }
}
