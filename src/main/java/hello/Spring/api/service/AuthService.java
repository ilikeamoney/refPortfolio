package hello.Spring.api.service;

import hello.Spring.api.crypto.PasswordEncoder;
import hello.Spring.api.crypto.SCryptPasswordEncoder;
import hello.Spring.api.domain.Member;
import hello.Spring.api.domain.Session;
import hello.Spring.api.exception.AlreadyExistEmailException;
import hello.Spring.api.exception.InvalidSignInformation;
import hello.Spring.api.repository.MemberRepository;
import hello.Spring.api.request.Login;
import hello.Spring.api.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

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
//        Member member = memberRepository
//                .findByEmailAndPassword(request.getEmail(), request.getPassword())
//                .orElseThrow(InvalidSignInformation::new);

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidSignInformation::new);



        // 요청으로 넘어온 비밀번호와 (암호화 되지 않음)
        // DB에 저장된 기존에 암호화된 비밀번호와 조회
        boolean matches = passwordEncoder.equalsPassword(request.getPassword(), member);

        if (!matches) {
            throw new InvalidSignInformation();
        }

        // Member Id 반환
        return member.getId();
    }


    public void signup(Signup signup) {
        Optional<Member> findMember = memberRepository.findByEmail(signup.getEmail());
        if (findMember.isPresent()) {
            throw new AlreadyExistEmailException();
        }

        String encryptedPassword = passwordEncoder.encrypt(signup.getPassword());

        var member = Member.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();

        memberRepository.save(member);
    }
}
