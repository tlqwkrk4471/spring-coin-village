package com.coinvillage.backend.domain.order.entry;

import lombok.Getter;

import java.util.Comparator;
import java.util.PriorityQueue;

@Getter
public class OrderBook {

    private final PriorityQueue<Order> buyOrders;
    private final PriorityQueue<Order> sellOrders;

    public OrderBook() {
        this.buyOrders = new PriorityQueue<>(
                Comparator.comparingLong(Order::getPrice).reversed()
                        .thenComparing(Order::getCreateAt)
                        .thenComparing(Order::getQuantity).reversed()
        );
        this.sellOrders = new PriorityQueue<>(
                Comparator.comparingLong(Order::getPrice)
                        .thenComparing(Order::getCreateAt)
                        .thenComparing(Order::getQuantity).reversed()
        );
    }

    public void addOrder(Order order) {
        if (order.getOrderSide() == OrderSide.BUY) {
            buyOrders.add(order);
        } else {
            sellOrders.add(order);
        }
    }

    public void removeOrder(Order order) {
        if (order.getOrderSide() == OrderSide.BUY) {
            buyOrders.remove(order);
        } else if (order.getOrderSide() == OrderSide.SELL) {
            sellOrders.remove(order);
        }
    }

    public Order peekBuyOrder() {
        return buyOrders.peek();
    }

    public Order peekSellOrder() {
        return sellOrders.peek();
    }

    public Order pollBuyOrder() {
        return buyOrders.poll();
    }

    public Order pollSellOrder() {
        return sellOrders.poll();
    }
}
