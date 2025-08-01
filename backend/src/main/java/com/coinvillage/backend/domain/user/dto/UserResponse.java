package com.coinvillage.backend.domain.user.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserResponse {

    private String name;

    public UserResponse() {}

    public UserResponse(String name) {
        this.name = name;
    }
}
