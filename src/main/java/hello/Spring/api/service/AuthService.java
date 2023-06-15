package hello.Spring.api.service;

import hello.Spring.api.domain.Member;
import hello.Spring.api.domain.Session;
import hello.Spring.api.exception.InvalidSignInformation;
import hello.Spring.api.repository.MemberRepository;
import hello.Spring.api.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final MemberRepository memberRepository;

    public void signIn(Login request) {
        Member member = memberRepository
                .findByEmailAndPassword(request.getEmail(), request.getPassword())
                .orElseThrow(InvalidSignInformation::new);

        member.addSession();

    }



}
