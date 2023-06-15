package hello.Spring.api.controller;

import hello.Spring.api.request.Login;
import hello.Spring.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 로그인 성공시 발급한 토큰값으로 현재 상태를 유지하는거 같음

    // http HEADER 에 세션값을 담아서 요청의 인증을 받는법도 많이 쓴다.
    // 그리고 쿠키도 많이 사용한다.

    @PostMapping("/auth/login")
    public ResponseEntity<Object > login(@RequestBody Login login) {
        String accessToken = authService.signIn(login);

        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost") // todo 서버 환경에 따른 분리  필요
                .path("/")
                .httpOnly(true)
                .secure(true)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();

        log.info(">>> cookie = {}", cookie);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .build();

    }

}
