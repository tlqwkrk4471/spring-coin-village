package com.coinvillage.backend.domain.order.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderResponseByCoin {

    private int price;
    private long buyQuantity;
    private long sellQuantity;

    public OrderResponseByCoin(int price, long buyQuantity, long sellQuantity) {
        this.price = price;
        this.buyQuantity = buyQuantity;
        this.sellQuantity = sellQuantity;
    }
}
