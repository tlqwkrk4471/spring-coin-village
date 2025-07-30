package com.coinvillage.backend.domain.user.service;

import com.coinvillage.backend.domain.coin.Coin;
import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void initWallet(User user) {
        user.getWallet().addCash(100000L);
    }

    @Override
    public Long getCashBalance(User user) {
        return user.getWallet().getCashBalance();
    }

    @Override
    public Map<Coin, Long> getCoinBalance(User user) {
        return user.getWallet().getCoinBalance();
    }
}
