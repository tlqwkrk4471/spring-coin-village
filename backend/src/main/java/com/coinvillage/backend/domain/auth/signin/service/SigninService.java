package com.coinvillage.backend.domain.auth.signin.service;

import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.auth.signin.dto.SigninRequest;

public interface SigninService {

    User signin(SigninRequest signinRequest);
}
