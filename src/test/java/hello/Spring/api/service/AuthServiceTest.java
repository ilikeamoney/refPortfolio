package hello.Spring.api.service;

import hello.Spring.api.crypto.PasswordEncoder;
import hello.Spring.api.domain.Member;
import hello.Spring.api.exception.AlreadyExistEmailException;
import hello.Spring.api.exception.InvalidSignInformation;
import hello.Spring.api.repository.MemberRepository;
import hello.Spring.api.request.Login;
import hello.Spring.api.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void deletePost() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signup() throws Exception {
        // given
        Signup signup = Signup.builder()
                .name("짭종")
                .email("2721000@naver.com")
                .password("1234")
                .build();

        // when
        authService.signup(signup);

        // then
        assertThat(memberRepository.count())
                .isEqualTo(1L);

        Member findMember = memberRepository.findAll().iterator().next();

        assertThat(findMember.getEmail())
                .isEqualTo("2721000@naver.com");

        assertThat(findMember.getName())
                .isEqualTo("짭종");

        assertThat(passwordEncoder.equalsPassword(signup.getPassword(), findMember))
                .isTrue();

        assertThat(findMember.getPassword())
                .isNotNull();
    }

    @Test
    @DisplayName("회원가입시 중복된 이메일")
    void existEmail() throws Exception {
        // given
        Member member = Member.builder()
                .email("ilikeamoney@gmail.com")
                .password("1234")
                .name("짭종")
                .build();

        memberRepository.save(member);

        Signup signup = Signup.builder()
                .email("ilikeamoney@gmail.com")
                .password("1234")
                .name("짭종")
                .build();

        // expected
        assertThatThrownBy(() -> authService.signup(signup))
                .isInstanceOf(AlreadyExistEmailException.class);

    }

    @Test
    @DisplayName("로그인 성공")
    void loginSuccess() throws Exception {
        // given
        String encryptedPassword = passwordEncoder.encrypt("1234");


        Member member = Member.builder()
                .email("ilikeamoney@gmail.com")
                .password(encryptedPassword)
                .name("짭종")
                .build();

        memberRepository.save(member);

        Login login = Login.builder()
                .email("ilikeamoney@gmail.com")
                .password("1234")
                .build();

        // when
        Long memberId = authService.signInId(login);

        // then
        assertThat(memberId).isEqualTo(1L);
    }

    @Test
    @DisplayName("로그인 비밀번호 불일치")
    void loginFail() throws Exception {
        // given
        String encryptedPassword = passwordEncoder.encrypt("1234");

        Member member = Member.builder()
                .email("ilikeamoney@gmail.com")
                .password(encryptedPassword)
                .name("짭종")
                .build();

        memberRepository.save(member);

        Login login = Login.builder()
                .email("ilikeamoney@gmail.com")
                .password("12345")
                .build();

        // expected
        assertThatThrownBy(() -> authService.signInId(login))
                .isInstanceOf(InvalidSignInformation.class);
    }
}