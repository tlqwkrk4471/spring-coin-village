package com.coinvillage.backend.config;

import com.coinvillage.backend.domain.coin.entry.Coin;
import com.coinvillage.backend.domain.coin.repository.CoinRepository;
import com.coinvillage.backend.domain.order.entry.Order;
import com.coinvillage.backend.domain.order.entry.OrderSide;
import com.coinvillage.backend.domain.order.entry.OrderType;
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
        // 코인 생성
        Coin btc = new Coin("BTC", "Bit Coin", 2900, 3200L);
        Coin bsc = new Coin("BSC", "Base Coin", 2400, 2000L);
        coinRepository.save(btc);
        coinRepository.save(bsc);

        // 시스템 관리자 유저
        User system = new User("system", "admin", "admin123");
        system.getWallet().addCoin(btc, 3200L);
        system.getWallet().addCoin(bsc, 4000L);
        userRepository.save(system);

        // BTC 초기 지정가 매도 주문
        orderService.placeOrder(new Order(system, btc, 2900, 490L, OrderSide.SELL, OrderType.LIMIT));
        orderService.placeOrder(new Order(system, btc, 2950, 870L, OrderSide.SELL, OrderType.LIMIT));
        orderService.placeOrder(new Order(system, btc, 3000, 1840L, OrderSide.SELL, OrderType.LIMIT));

        // BSC 초기 지정가 매도 주문
        orderService.placeOrder(new Order(system, bsc, 2400, 490L, OrderSide.SELL, OrderType.LIMIT));
        orderService.placeOrder(new Order(system, bsc, 2450, 1070L, OrderSide.SELL, OrderType.LIMIT));
        orderService.placeOrder(new Order(system, bsc, 2500, 440L, OrderSide.SELL, OrderType.LIMIT));

        // 일반 사용자
        User user1 = new User("user1", "user1", "123");
        user1.getWallet().addCash(1000000L);
        userRepository.save(user1);

        User user2 = new User("user2", "user2", "123");
        user2.getWallet().addCash(1000000L);
        userRepository.save(user2);
    }
}
