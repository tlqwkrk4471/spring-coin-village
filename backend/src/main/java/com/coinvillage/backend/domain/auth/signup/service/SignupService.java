package com.coinvillage.backend.domain.auth.signup.service;

import com.coinvillage.backend.domain.auth.signup.dto.SignupRequest;

public interface SignupService {

    boolean existLoginId(String LoginId);
    boolean signup(SignupRequest signupRequest);
}
