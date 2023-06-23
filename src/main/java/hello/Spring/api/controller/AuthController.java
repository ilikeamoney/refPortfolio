package hello.Spring.api.controller;

import hello.Spring.api.config.AppConfig;
import hello.Spring.api.request.Login;
import hello.Spring.api.request.Signup;
import hello.Spring.api.response.SessionResponse;
import hello.Spring.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.util.Base64;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private static final String KEY = "mK+hkhNo4/OwqHzOy2x/EZ4QYgmCZIseZuqhYsqtm14=";

    private final AppConfig appConfig;


    // 로그인 성공시 발급한 토큰값으로 현재 상태를 유지하는거 같음

    // http HEADER 에 세션값을 담아서 요청의 인증을 받는법도 많이 쓴다.
    // 그리고 쿠키도 많이 사용한다.

    @PostMapping("/auth/login")
    public ResponseEntity<Object > login(@RequestBody Login login) {
        String accessToken = authService.signInToken(login);

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

    /**
     * JWT 를 사용하여 암호화된 토큰값 사용하기
     * 서버 내부에서는 사용자 로그인 인증 값을 내부에 고정값으로 셋팅해두고 사용한다.
     */

    @PostMapping("/auth/login/1")
    public SessionResponse login1(@RequestBody Login login) {

        Long memberId = authService.signInId(login);

        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // 암호화 키 바이트로 변환
        byte[] keyEncoded = key.getEncoded();

        // 바이트 문자열로 변환
        String strKey = Base64.getEncoder().encodeToString(keyEncoded);

        SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));

        String jws = Jwts.builder()
                .setSubject(String.valueOf(memberId))
                .signWith(secretKey)
                .setIssuedAt(new Date())
                .compact();

         return new SessionResponse(jws);
    }


    @PostMapping("/auth/signup")
    public void signup(@RequestBody Signup signup) {
        authService.signup(signup);
    }

}
