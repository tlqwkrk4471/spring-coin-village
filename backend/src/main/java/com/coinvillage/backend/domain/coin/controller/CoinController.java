package com.coinvillage.backend.domain.coin.controller;

import com.coinvillage.backend.domain.coin.service.CoinServiceImpl;
import com.coinvillage.backend.domain.coin.dto.CoinResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/coin")
public class CoinController {

    private final CoinServiceImpl coinService;

    @Autowired
    public CoinController(CoinServiceImpl coinService) {
        this.coinService = coinService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<CoinResponse>> all() {
        List<CoinResponse> coins = coinService.getAllCoins();
        return ResponseEntity.ok(coins);
    }
}
