package com.coinvillage.backend.web.auth.signin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SigninRequest {

    private String loginId;
    private String password;
}
