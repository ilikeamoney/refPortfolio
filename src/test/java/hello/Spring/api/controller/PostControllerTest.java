package hello.Spring.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.Spring.api.domain.Post;
import hello.Spring.api.repository.PostRepository;
import hello.Spring.api.request.PostCreate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @AfterEach
    void deleteAll() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 Hello World ! 출력한다.")
    public void test() throws Exception {
        //given
        String json = objectMapper.writeValueAsString(PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build());

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(""))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 title 값은 필수.")
    public void test2() throws Exception {
        //given
        String request = objectMapper.writeValueAsString(
                PostCreate.builder()
                .content("내용입니다.")
                .build());

        //when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다"))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요"))
                .andDo(print());
    }

    @Test
    @DisplayName("/posts 요청시 DB에 값이 저장된다.")
    public void test3() throws Exception {
        //given
        String request = objectMapper.writeValueAsString(
                PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build());

        // when
        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(request)
                )
                .andExpect(status().isOk())
                .andDo(print());

        // then
        Assertions.assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
        assertThat(post.getContent()).isEqualTo("내용입니다.");
    }

    @Test
    @DisplayName("글 1개 조회")
    public void test4() throws Exception {
        //given
        Post request = postRepository.save(Post.builder()
                .title("123456789012345")
                .content("bar")
                .build());

        //expected
        mockMvc.perform(get("/posts/{postId}", request.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(request.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value(request.getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    public void test5() throws Exception {
        //given
        Post requestPost1 = Post.builder()
                .title("title_1")
                .content("content_1")
                .build();

        postRepository.save(requestPost1);

        Post requestPost2 = Post.builder()
                .title("title_2")
                .content("content_2")
                .build();

        postRepository.save(requestPost2);

        //when
        mockMvc.perform(get("/posts")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$[0].id").value(requestPost1.getId()))
                .andExpect(jsonPath("$[0].title").value("title_1"))
                .andExpect(jsonPath("$[0].content").value("content_1"))
                .andExpect(jsonPath("$[1].id").value(requestPost2.getId()))
                .andExpect(jsonPath("$[1].title").value("title_2"))
                .andExpect(jsonPath("$[1].content").value("content_2"))
                .andDo(print());
    }

}