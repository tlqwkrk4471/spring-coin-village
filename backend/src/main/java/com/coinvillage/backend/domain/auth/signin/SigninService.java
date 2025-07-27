package com.coinvillage.backend.domain.auth.signin;

import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.web.auth.signin.SigninRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface SigninService {

    User signin(SigninRequest signinRequest);
}
