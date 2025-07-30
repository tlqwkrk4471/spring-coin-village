package com.coinvillage.backend.web.user;

import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/wallet/coin")
    public Long getCoinBalance(@SessionAttribute(name = "loginUser", required = false) User loginUser) {
        if (loginUser == null) return null;

        return null;
        //return loginUser.getWallet().getCoinBalance();
    }

    @GetMapping("/wallet/cash")
    public Long getCashBalance(@SessionAttribute(name = "loginUser", required = false) User loginUser) {
        if (loginUser == null) return null;
        return loginUser.getWallet().getCashBalance();
    }
}
