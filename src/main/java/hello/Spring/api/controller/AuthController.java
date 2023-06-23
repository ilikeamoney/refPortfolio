package hello.Spring.api.controller;

import hello.Spring.api.request.Signup;
import hello.Spring.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public void  signup(@RequestBody Signup signup) {
        authService.signup(signup);
    }

    @GetMapping("/auth/login")
    public String login() {
        return "로그인 화면입니다.";
    }
}
