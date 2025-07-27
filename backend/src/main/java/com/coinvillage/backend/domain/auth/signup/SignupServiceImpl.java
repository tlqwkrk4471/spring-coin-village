package com.coinvillage.backend.domain.auth.signup;

import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.user.UserRepository;
import com.coinvillage.backend.web.auth.signup.SignupRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SignupServiceImpl implements SignupService {

    private final UserRepository userRepository;

    @Autowired
    public SignupServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

        user.getWallet().addCash(100000L);

        userRepository.save(user);
        log.info("signup: user={}", user);

        return true;
    }
}
