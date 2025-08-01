package com.coinvillage.backend.domain.user;

import com.coinvillage.backend.domain.coin.entry.Wallet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {

    private Long id;
    private String name;
    private String loginId;
    private String password;
    private Wallet wallet;

    public User() {
        wallet = new Wallet();
    }

    public User(String name, String loginId, String password) {
        this.name = name;
        this.loginId = loginId;
        this.password = password;
        this.wallet = new Wallet();
    }
}
