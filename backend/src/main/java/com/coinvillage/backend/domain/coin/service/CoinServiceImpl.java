package com.coinvillage.backend.domain.coin.service;

import com.coinvillage.backend.domain.coin.entry.Coin;
import com.coinvillage.backend.domain.coin.repository.CoinRepository;
import com.coinvillage.backend.domain.coin.dto.CoinResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CoinServiceImpl implements CoinService {

    private final CoinRepository coinRepository;

    @Autowired
    public CoinServiceImpl(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    public List<CoinResponse> getAllCoins() {
        List<Coin> coinList = coinRepository.findAll();
        return coinList.stream()
                .map(coin -> new CoinResponse(coin.getSymbol(), coin.getName(), coin.getCurrentPrice()))
                .collect(Collectors.toList());
    }
}
