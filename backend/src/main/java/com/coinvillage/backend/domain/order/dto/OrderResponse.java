package com.coinvillage.backend.domain.order.dto;

import com.coinvillage.backend.domain.order.entry.Order;
import com.coinvillage.backend.domain.order.entry.OrderSide;
import com.coinvillage.backend.domain.order.entry.OrderStatus;
import com.coinvillage.backend.domain.order.entry.OrderType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class OrderResponse {

    private UUID id;
    private String coinSymbol;
    private String coinName;
    private Integer price;
    private Long quantity;
    private OrderSide orderSide;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private LocalDateTime createAt;

    public OrderResponse() {}

    public OrderResponse(Order order) {
        this.id = order.getId();
        this.coinSymbol = order.getCoin().getSymbol();
        this.coinName = order.getCoin().getName();
        this.price = order.getPrice();
        this.quantity = order.getQuantity();
        this.orderSide = order.getOrderSide();
        this.orderType = order.getOrderType();
        this.orderStatus = order.getOrderStatus();
        this.createAt = order.getCreateAt();
    }

    public static OrderResponse from(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setCoinSymbol(order.getCoin().getSymbol());
        response.setCoinName(order.getCoin().getName());
        response.setPrice(order.getPrice());
        response.setQuantity(order.getQuantity());
        response.setOrderSide(order.getOrderSide());
        response.setOrderType(order.getOrderType());
        response.setOrderStatus(order.getOrderStatus());
        response.setCreateAt(order.getCreateAt());
        return response;
    }
}
