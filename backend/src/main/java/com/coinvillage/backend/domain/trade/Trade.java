package com.coinvillage.backend.domain.trade;

import com.coinvillage.backend.domain.coin.entry.Coin;
import com.coinvillage.backend.domain.order.entry.Order;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
public class Trade {
    private UUID tradeId;
    private Order buyOrder;
    private Order sellOrder;
    private Coin coin;
    private Integer price;
    private Long quantity;
    private LocalDateTime localDateTime;

    public Trade() {}

    public Trade(Order buyOrder, Order sellOrder, Integer price, Long quantity) {
        this.tradeId = UUID.randomUUID();
        this.buyOrder = buyOrder;
        this.sellOrder = sellOrder;
        this.coin = buyOrder.getCoin();
        this.price = price;
        this.quantity = quantity;
        this.localDateTime = LocalDateTime.now();
    }
}
