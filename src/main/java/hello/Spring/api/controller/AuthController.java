package hello.Spring.api.controller;

import hello.Spring.api.domain.Member;
import hello.Spring.api.exception.InvalidSignInformation;
import hello.Spring.api.repository.MemberRepository;
import hello.Spring.api.request.Login;
import hello.Spring.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.message.AuthException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 로그인 성공시 발급한 토큰값으로 현재 상태를 유지하는거 같음

    @PostMapping("/auth/login")
    public void login(@RequestBody Login login) {
        authService.signIn(login);
    }

}
