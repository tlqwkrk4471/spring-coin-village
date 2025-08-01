package com.coinvillage.backend.domain.coin.entry;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Coin {

    private String symbol; //primary key
    private String name;
    private Integer currentPrice;
    private Long totalQuantity;

    public Coin() {
    }

    public Coin(String symbol, String name, Integer currentPrice, Long totalQuantity) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
        this.totalQuantity = getTotalQuantity();
    }
}
