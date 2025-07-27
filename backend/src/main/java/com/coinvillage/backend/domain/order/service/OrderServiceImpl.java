package com.coinvillage.backend.domain.order.service;

import com.coinvillage.backend.domain.coin.Coin;
import com.coinvillage.backend.domain.order.Order;
import com.coinvillage.backend.domain.order.OrderSide;
import com.coinvillage.backend.domain.order.repository.OrderRepository;
import com.coinvillage.backend.domain.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Boolean placeOrder(Order order) {
        Long cashBalance = order.getUser().getWallet().getCashBalance();
        long totalPrice = order.getPrice() * order.getQuantity();

        if (order.getOrderSide() == OrderSide.BUY) {
            if (cashBalance - totalPrice >= 0) {
                orderRepository.save(order);
            } else {
                return false;
            }
        } else if (order.getOrderSide() == OrderSide.SELL) {
            orderRepository.save(order);
        }

        return true;
    }

    @Override
    public Boolean cancelOrder(UUID id) {
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
}
