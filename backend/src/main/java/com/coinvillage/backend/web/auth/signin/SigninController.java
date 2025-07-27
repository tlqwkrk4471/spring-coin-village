package com.coinvillage.backend.web.auth.signin;

import com.coinvillage.backend.domain.auth.signin.SigninService;
import com.coinvillage.backend.domain.user.User;
import com.coinvillage.backend.web.user.UserResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth/signin")
public class SigninController {

    private final SigninService signinService;

    @Autowired
    public SigninController(SigninService signinService) {
        this.signinService = signinService;
    }

    /**
     * 로그인
     * @return 로그인 성공 여부
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("")
    public Boolean signin(@RequestBody SigninRequest signinRequest,
                                          HttpServletRequest request) {
        User user = signinService.signin(signinRequest);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("loginUser", user);

            return true;
        }
        return false;
    }

    /**
     * 로그인된 유저 정보 처리
     * @return 로그인된 유저 정보
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/session")
    public UserResponse responseUser(@SessionAttribute(name = "loginUser", required = false) User loginUser) {
        if (loginUser == null) {
            return null;
        }

        UserResponse userResponse = new UserResponse(loginUser.getName());
        return userResponse;
    }

    /**
     * 로그아웃
     * @return 로그아웃 성공 여부
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/logout")
    public Boolean logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            log.info("logout: {}", (User)session.getAttribute("loginUser"));
            session.invalidate();
        }
        return true;
    }
}
