package hello.Spring.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.Spring.api.domain.Member;
import hello.Spring.api.domain.Session;
import hello.Spring.api.exception.InvalidSignInformation;
import hello.Spring.api.repository.MemberRepository;
import hello.Spring.api.repository.SessionRepository;
import hello.Spring.api.request.Login;
import hello.Spring.api.request.Signup;
import org.assertj.core.api.Assertions;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 테스트")
    void test() throws Exception {
        //given
        memberRepository.save(Member.builder()
                .name("zzabjong")
                .email("ilikeamoney@gmail.com")
                .password("1234")
                .build());

        String json = objectMapper.writeValueAsString(Login.builder()
                .email("ilikeamoney@gmail.com")
                .password("1234")
                .build());

        //when
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @Transactional
    @DisplayName("로그인 성공 후 세션확인")
    void test1() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("zzabjong")
                .email("ilikeamoney@gmail.com")
                .password("1234")
                .build());

        String json = objectMapper.writeValueAsString(Login.builder()
                .email("ilikeamoney@gmail.com")
                .password("1234")
                .build());

        //when
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertThat(member.getSessions().size()).isEqualTo(1L);
    }

    @Test
    @DisplayName("로그인 성공 후 세션응답")
    void test2() throws Exception {
        //given
        Member member = memberRepository.save(Member.builder()
                .name("zzabjong")
                .email("ilikeamoney@gmail.com")
                .password("1234")
                .build());

        String json = objectMapper.writeValueAsString(Login.builder()
                .email("ilikeamoney@gmail.com")
                .password("1234")
                .build());

        //when
        mockMvc.perform(post("/auth/login")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accessToken", notNullValue()))
                .andDo(print());
    }
    
    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지에 접속한다")
    void test3() throws Exception {
        // given
        Member member = Member.builder()
                .name("짭종")
                .email("ilikeamoney2@gmail.com")
                .password("1234")
                .build();

        Session session = member.addSession();
        memberRepository.save(member);

        // when
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이제 접속할 수 없다.")
    void test4() throws Exception {
        // given
        Member member = Member.builder()
                .name("짭종")
                .email("ilikeamoney2@gmail.com")
                .password("1234")
                .build();

        Session session = member.addSession();
        memberRepository.save(member);

        // when
        mockMvc.perform(get("/foo")
                        .header("Authorization", session.getAccessToken() + "qwe1234")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }


    @Test
    @DisplayName("회원가입 요청")
    void signup() throws Exception {
        // given
        Signup signup = Signup.builder()
                .email("ilikeamoney@gmail.com")
                .password("1234")
                .name("짭종")
                .build();

        String json = objectMapper.writeValueAsString(signup);

        // expected
        mockMvc.perform(post("/auth/signup")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andDo(print());

        Assertions.assertThat(memberRepository.count())
                .isEqualTo(1L);
    }
}