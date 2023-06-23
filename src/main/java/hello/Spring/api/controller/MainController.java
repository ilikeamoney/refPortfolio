package hello.Spring.api.controller;

import hello.Spring.api.config.MemberPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/")
    public String main() {
        return "메인 페이지 입니다.";
    }

    @GetMapping("/member")
    public String member(@AuthenticationPrincipal MemberPrincipal memberPrincipal) {
        return "사용자 페이지 입니다. 😂";
    }

    @GetMapping("/admin")
    public String admin() {
        return "관리자 페이지 입니다. 😍";
    }
}
