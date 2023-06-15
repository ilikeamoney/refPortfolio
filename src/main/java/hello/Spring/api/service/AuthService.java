package hello.Spring.api.service;

import hello.Spring.api.domain.Member;
import hello.Spring.api.domain.Session;
import hello.Spring.api.exception.InvalidSignInformation;
import hello.Spring.api.repository.MemberRepository;
import hello.Spring.api.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    @Transactional
    public String signInToken(Login request) {
        Member member = memberRepository
                .findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSignInformation::new);

        Session session = member.addSession();

        // 토큰값 반환
        return session.getAccessToken();
    }

    @Transactional
    public Long signInId(Login request) {
        Member member = memberRepository
                .findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSignInformation::new);

        // Member Id 반환
        return member.getId();
    }



}
