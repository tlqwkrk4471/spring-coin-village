package com.coinvillage.backend.domain.order.service;

import com.coinvillage.backend.domain.coin.Coin;
import com.coinvillage.backend.domain.order.Order;
import com.coinvillage.backend.domain.order.OrderBookManager;
import com.coinvillage.backend.domain.order.OrderSide;
import com.coinvillage.backend.domain.order.OrderStatus;
import com.coinvillage.backend.domain.order.repository.OrderRepository;
import com.coinvillage.backend.domain.trade.service.TradeService;
import com.coinvillage.backend.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderBookManager orderBookManager;
    private final TradeService tradeService;

    @Override
    public Boolean placeOrder(Order order) {
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
    public List<Order> getFilledOrdersByUser(User user) {
        return orderRepository.findFilledOrdersByUser(user);
    }

    @Override
    public List<Order> getFilledOrdersByCoin(Coin coin) {
        return orderRepository.findFilledOrdersByCoin(coin);
    }
}
