package com.coinvillage.backend.domain.auth.signup.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {

    private String username;
    private String loginId;
    private String password;
}
