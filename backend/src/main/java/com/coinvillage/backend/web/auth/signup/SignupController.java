package com.coinvillage.backend.web.auth.signup;

import com.coinvillage.backend.domain.auth.signup.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/signup")
public class SignupController {

    private final SignupService signupService;

    @Autowired
    public SignupController(SignupService signupService) {
        this.signupService = signupService;
    }

    /**
     * 회원가입
     * @return 회원가입 성공 여부
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("")
    public Boolean signup(@RequestBody SignupRequest signupRequest) {
        return signupService.signup(signupRequest);
    }

    /**
     * 아이디 중복 확인
     * @return 아이디 중복 여부
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/check-id")
    public Boolean checkLoginId(@RequestParam String loginId) {
        return signupService.existLoginId(loginId);
    }
}
