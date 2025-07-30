package com.coinvillage.backend.config;

import com.coinvillage.backend.domain.coin.Coin;
import com.coinvillage.backend.domain.coin.repository.CoinRepository;
import com.coinvillage.backend.domain.order.Order;
import com.coinvillage.backend.domain.order.OrderSide;
import com.coinvillage.backend.domain.order.OrderType;
import com.coinvillage.backend.domain.order.service.OrderService;
import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.user.repository.UserRepository;
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
    private final OrderService orderService;

    @PostConstruct
    public void init() {
        Coin btc = new Coin("BTC", "Bit Coin", 3000, 3200L);
        Coin bsc = new Coin("BSC", "Base Coin", 2500, 4000L);
        coinRepository.save(bsc);
        coinRepository.save(btc);

        User user1 = new User("user1", "user1", "123");
        user1.getWallet().addCash(100000L);
        user1.getWallet().addCoin(btc, 3200L);
        user1.getWallet().addCoin(bsc, 4000L);
        userRepository.save(user1);
        Order order1 = new Order(user1, btc, 3100, 20L, OrderSide.SELL, OrderType.LIMIT);
        orderService.placeOrder(order1);

        User user2 = new User("user2", "user2", "123");
        user2.getWallet().addCash(100000L);
        userRepository.save(user2);
    }

}
