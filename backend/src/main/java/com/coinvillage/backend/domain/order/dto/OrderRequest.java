package com.coinvillage.backend.domain.order.dto;

import com.coinvillage.backend.domain.order.entry.OrderSide;
import com.coinvillage.backend.domain.order.entry.OrderType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrderRequest {

    private String coinSymbol;
    private OrderSide orderSide;
    private OrderType orderType;
    private Integer price;
    private Long quantity;
}
