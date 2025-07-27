package com.coinvillage.backend.domain.coin.repository;

import com.coinvillage.backend.domain.coin.Coin;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemoryCoinRepository implements CoinRepository {

    private final Map<String, Coin> store = new HashMap<>();

    @Override
    public Coin save(Coin coin) {
        store.put(coin.getSymbol(), coin);
        log.info("save: coin={}", coin);
        return coin;
    }

    @Override
    public Optional<Coin> findBySymbol(String symbol) {
        return Optional.ofNullable(store.get(symbol));
    }

    @Override
    public List<Coin> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public void clear() {
        store.clear();
    }
}
