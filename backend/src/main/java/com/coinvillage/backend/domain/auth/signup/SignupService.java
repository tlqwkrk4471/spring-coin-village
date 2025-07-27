package com.coinvillage.backend.domain.auth.signup;

import com.coinvillage.backend.web.auth.signup.SignupRequest;

public interface SignupService {

    boolean existLoginId(String LoginId);
    boolean signup(SignupRequest signupRequest);
}
