package com.coinvillage.backend.domain.user;

import com.coinvillage.backend.domain.coin.Wallet;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class User {

    private Long id; //primary key
    private String name;
    private String loginId;
    private String password;
    private Wallet wallet = new Wallet();

    public User() {}
}
