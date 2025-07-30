package com.coinvillage.backend.domain.order;

import com.coinvillage.backend.domain.order.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderBookInitializer {

    private final OrderRepository orderRepository;
    private final OrderBookManager orderBookManager;

    @Autowired
    public OrderBookInitializer(OrderRepository orderRepository, OrderBookManager orderBookManager) {
        this.orderRepository = orderRepository;
        this.orderBookManager = orderBookManager;
    }

    @PostConstruct
    public void initializeOrderBooks() {
        List<Order> allOrders = orderRepository.findAll();

        for (Order order : allOrders) {
            if (order.getOrderStatus() == OrderStatus.PENDING || order.getOrderStatus() == OrderStatus.PARTIAL_FILLED) {
                orderBookManager.getOrderBook(order.getCoin()).addOrder(order);
            }
        }
    }
}