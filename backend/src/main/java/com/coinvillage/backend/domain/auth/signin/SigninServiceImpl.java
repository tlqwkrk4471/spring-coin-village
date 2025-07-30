package com.coinvillage.backend.domain.auth.signin;

import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.domain.user.repository.UserRepository;
import com.coinvillage.backend.web.auth.signin.SigninRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SigninServiceImpl implements SigninService {

    private final UserRepository userRepository;

    @Autowired
    public SigninServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * @param signinRequest
     * @return null이면 로그인 실패
     */
    @Override
    public User signin(SigninRequest signinRequest) {
        User loginUser = userRepository.findByLoginId(signinRequest.getLoginId())
                .filter(user -> user.getPassword().equals(signinRequest.getPassword()))
                .orElse(null);
        log.info("signin: user={}", loginUser);

        return loginUser;
    }
}
