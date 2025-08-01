package com.coinvillage.backend.domain.auth.signin.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {

    private String loginId;
    private String password;
}
