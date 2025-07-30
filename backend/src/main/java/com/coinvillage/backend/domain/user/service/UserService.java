package com.coinvillage.backend.domain.user.service;

import com.coinvillage.backend.domain.coin.Coin;
import com.coinvillage.backend.domain.user.User;

import java.util.Map;

public interface UserService {

    void initWallet(User user);
    Long getCashBalance(User user);
    Map<Coin, Long> getCoinBalance(User user);
}
