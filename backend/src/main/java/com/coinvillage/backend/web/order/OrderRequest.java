package com.coinvillage.backend.web.order;

import com.coinvillage.backend.domain.order.OrderSide;
import com.coinvillage.backend.domain.order.OrderType;
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
