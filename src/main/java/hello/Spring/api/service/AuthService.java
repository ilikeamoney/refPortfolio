package hello.Spring.api.service;

import hello.Spring.api.domain.Member;
import hello.Spring.api.exception.AlreadyExistEmailException;
import hello.Spring.api.repository.MemberRepository;
import hello.Spring.api.request.Signup;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;


    public void signup(Signup signup) {
        Optional<Member> findMember = memberRepository.findByEmail(signup.getEmail());
        if (findMember.isPresent()) {
            throw new AlreadyExistEmailException();
        }

        String encryptedPassword = passwordEncoder.encode(signup.getPassword());

        var member = Member.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();

        memberRepository.save(member);
    }
}
