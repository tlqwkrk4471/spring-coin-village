package com.coinvillage.backend.config;

import com.coinvillage.backend.domain.coin.Coin;
import com.coinvillage.backend.domain.coin.repository.CoinRepository;
import com.coinvillage.backend.domain.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final CoinRepository coinRepository;
    private final UserRepository userRepository;

    @PostConstruct
    public void init() {
        Coin btc = new Coin("BTC", "Bit Coin", 3000, 3200L);
        Coin bsc = new Coin("BSC", "Base Coin", 2500, 4000L);
        coinRepository.save(bsc);
        coinRepository.save(btc);
    }

}
