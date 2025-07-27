package com.coinvillage.backend.domain.coin.service;

import com.coinvillage.backend.web.coin.CoinResponse;

import java.util.List;

public interface CoinService {

    public List<CoinResponse> getAllCoins();
}
