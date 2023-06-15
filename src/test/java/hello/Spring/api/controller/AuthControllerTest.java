package hello.Spring.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.Spring.api.domain.Member;
import hello.Spring.api.domain.Session;
import hello.Spring.api.exception.InvalidSignInformation;
import hello.Spring.api.repository.MemberRepository;
import hello.Spring.api.repository.SessionRepository;
import hello.Spring.api.request.Login;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("로그인 성공 후 세션확인")
    void test1() throws Exception {
        //given
        Member findMember = memberRepository.save(Member.builder()
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andDo(print());

    }
}