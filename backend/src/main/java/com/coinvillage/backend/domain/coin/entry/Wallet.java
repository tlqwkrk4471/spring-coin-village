package com.coinvillage.backend.domain.coin.entry;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@ToString
public class Wallet {

    private Map<Coin, Long> coinBalance = new HashMap<>();
    private Long cashBalance = 0L;

    public Wallet() {
    }

    public Long getTotalCoinBalance() {
        long total = 0;
        for (Map.Entry<Coin, Long> entry : coinBalance.entrySet()) {
            Coin coin = entry.getKey();
            Long amount = entry.getValue();
            Integer price = coin.getCurrentPrice();
            total += amount * price;
        }
        return total;
    }

    public Long getTotalAsset() {
        return getTotalCoinBalance() + getCashBalance();
    }

    public void addCoin(Coin coin, long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("추가할 코인 수량은 음수일 수 없습니다.");
        }
        coinBalance.put(coin, getCoinBalanceByCoin(coin) + amount);
    }

    public void substractCoin(Coin coin, long amount) {
        long current = getCoinBalanceByCoin(coin);
        if (amount < 0) {
            throw new IllegalArgumentException("차감할 코인 수량은 음수일 수 없습니다.");
        }
        if (current < amount) {
            throw new IllegalArgumentException("보유 코인이 부족합니다.");
        }
        coinBalance.put(coin, current - amount);
    }

    public long getCoinBalanceByCoin(Coin coin) {
        return coinBalance.getOrDefault(coin, 0L);
    }

    public long getCoinBalanceBySymbol(String symbol) {
        return coinBalance.entrySet().stream()
                .filter(entry -> entry.getKey().getSymbol().equals(symbol))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(0L);
    }

    public void addCash(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("추가할 금액은 음수일 수 없습니다.");
        }
        cashBalance += amount;
    }

    public void substractCash(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("차감할 금액은 음수일 수 없습니다.");
        }
        if (cashBalance < amount) {
            throw new IllegalArgumentException("보유 현금이 부족합니다.");
        }
        cashBalance -= amount;
    }

    public void clear() {
        coinBalance.clear();
        cashBalance = 0L;
    }
}
