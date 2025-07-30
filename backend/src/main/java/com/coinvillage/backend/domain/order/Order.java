package com.coinvillage.backend.domain.order;

import com.coinvillage.backend.domain.coin.Coin;
import com.coinvillage.backend.domain.user.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Order {

    private UUID id;
    private User user;
    private Coin coin;
    private Integer price;
    private Long quantity;
    private Long filledQuantity;
    private OrderSide orderSide;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private LocalDateTime createAt;

    public Order() {
        this.id = UUID.randomUUID();
        this.orderStatus = OrderStatus.PENDING;
        this.createAt = LocalDateTime.now();
    }

    public Order(User user, Coin coin, int price, long quantity, OrderSide orderSide, OrderType orderType) {
        this.id = UUID.randomUUID();
        this.user = user;
        this.coin = coin;
        this.price = price;
        this.quantity = quantity;
        this.filledQuantity = 0L;
        this.orderSide = orderSide;
        this.orderType = orderType;
        this.orderStatus = OrderStatus.PENDING;
        this.createAt = LocalDateTime.now();
    }
}
