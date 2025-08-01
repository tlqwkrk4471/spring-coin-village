package com.coinvillage.backend.domain.auth.signup.service;

import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.user.repository.UserRepository;
import com.coinvillage.backend.domain.user.service.UserService;
import com.coinvillage.backend.domain.auth.signup.dto.SignupRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SignupServiceImpl implements SignupService {

    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public boolean existLoginId(String loginId) {
        return userRepository.existLoginId(loginId);
    }

    @Override
    public boolean signup(SignupRequest signupRequest) {
        User user = new User();
        user.setName(signupRequest.getUsername());
        user.setLoginId(signupRequest.getLoginId());
        user.setPassword(signupRequest.getPassword());

        userService.initWallet(user);

        userRepository.save(user);
        log.info("signup: user={}", user);

        return true;
    }
}
