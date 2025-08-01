package com.coinvillage.backend.domain.coin.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserCoinResponse {

    private String symbol;
    private String name;
    private Integer price;
    private Long quantity;

    public UserCoinResponse() {}

    public UserCoinResponse(String symbol, String name, Integer price, Long quantity) {
        this.symbol = symbol;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }
}
