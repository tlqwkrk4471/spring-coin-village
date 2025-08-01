package com.coinvillage.backend.domain.coin.repository;

import com.coinvillage.backend.domain.coin.entry.Coin;

import java.util.List;
import java.util.Optional;

public interface CoinRepository {

    Coin save(Coin coin);
    Optional<Coin> findBySymbol(String symbol);
    List<Coin> findAll();
    void clear();
}
