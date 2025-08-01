package com.coinvillage.backend.domain.user.controller;

import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.user.dto.UserAssetResponse;
import com.coinvillage.backend.domain.user.service.UserService;
import com.coinvillage.backend.domain.coin.dto.UserCoinResponse;
import com.coinvillage.backend.domain.user.dto.UserResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/ranking")
    public List<UserAssetResponse> getUserRankingByTotalAsset() {
        return userService.getUserRankingByTotalAsset();
    }

    @GetMapping("/wallet/coin")
    public List<UserCoinResponse> getCoinBalance(@SessionAttribute(name = "loginUser", required = false) User loginUser) {
        return userService.getCoinBalance(loginUser);
    }

    @GetMapping("/wallet/cash")
    public Long getCashBalance(@SessionAttribute(name = "loginUser", required = false) User loginUser) {
        return userService.getCashBalance(loginUser);
    }
}
