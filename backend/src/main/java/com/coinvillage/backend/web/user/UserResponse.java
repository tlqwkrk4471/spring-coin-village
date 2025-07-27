package com.coinvillage.backend.web.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {

    private String name;

    public UserResponse() {}

    public UserResponse(String name) {
        this.name = name;
    }
}
