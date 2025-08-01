package com.coinvillage.backend.domain.coin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CoinResponse {

    private String symbol;
    private String name;
    private Integer currentPrice;

    public CoinResponse() {}

    public CoinResponse(String symbol, String name, int currentPrice) {
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = currentPrice;
    }
}
