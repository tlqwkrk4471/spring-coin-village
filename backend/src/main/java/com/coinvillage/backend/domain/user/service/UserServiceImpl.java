package com.coinvillage.backend.domain.user.service;

import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.user.dto.UserAssetResponse;
import com.coinvillage.backend.domain.user.repository.UserRepository;
import com.coinvillage.backend.domain.coin.dto.UserCoinResponse;
import com.coinvillage.backend.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<UserAssetResponse> getUserRankingByTotalAsset() {
        return userRepository.findAll().stream()
                .filter(user -> !user.getName().equals("system"))
                .sorted((u1, u2) -> Long.compare(
                        getTotalAsset(u2), getTotalAsset(u1)))
                .map(user -> new UserAssetResponse(user.getName(), user.getWallet().getTotalAsset()))
                .toList();
    }

    private long getTotalAsset(User user) {
        long coinValue = user.getWallet().getCoinBalance().entrySet().stream()
                .mapToLong(entry -> entry.getKey().getCurrentPrice() * entry.getValue())
                .sum();
        return coinValue + user.getWallet().getCashBalance();
    }

    @Override
    public void initWallet(User user) {
        user.getWallet().addCash(1000000L);
    }

    @Override
    public Long getCashBalance(User user) {
        if (user == null) return null;
        return user.getWallet().getCashBalance();
    }

    @Override
    public List<UserCoinResponse> getCoinBalance(User user) {
        if (user == null) return null;
        return user.getWallet().getCoinBalance().entrySet().stream()
                .map(entry -> new UserCoinResponse(
                        entry.getKey().getSymbol(),
                        entry.getKey().getName(),
                        entry.getKey().getCurrentPrice(),
                        entry.getValue()))
                .toList();
    }
}
