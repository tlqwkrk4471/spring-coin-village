package com.coinvillage.backend.domain.user.service;

import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.coin.dto.UserCoinResponse;
import com.coinvillage.backend.domain.user.dto.UserAssetResponse;
import com.coinvillage.backend.domain.user.dto.UserResponse;

import java.util.List;

public interface UserService {

    List<UserAssetResponse> getUserRankingByTotalAsset();
    void initWallet(User user);
    Long getCashBalance(User user);
    List<UserCoinResponse> getCoinBalance(User user);
}
