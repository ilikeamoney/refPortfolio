package hello.Spring.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.Spring.api.domain.Post;
import hello.Spring.api.repository.PostRepository;
import hello.Spring.api.request.PostCreate;
import hello.Spring.api.request.PostEdit;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    @BeforeEach
    void BeforeDeleteAllPost() {
        postRepository.deleteAll();
    }

    @AfterEach
    void AfterDeleteAllPost() {
        postRepository.deleteAll();
    }


    @Test
    @DisplayName("/posts 요청시 Hello World ! 출력한다.")
    void test() throws Exception {
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
    void test2() throws Exception {
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
    void test3() throws Exception {
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
    void test4() throws Exception {
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
    void test5() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0, 30)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("제목 - " + i)
                            .content("내용 - " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //when
        // 웹 요청으로 정렬 조건을 넘길때
        mockMvc.perform(get("/posts?page=1&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(33))
                .andExpect(jsonPath("$[0].title").value(requestPosts.get(29).getTitle()))
                .andExpect(jsonPath("$[0].content").value(requestPosts.get(29).getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("0 페이지로 요청")
    void test6() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(0, 30)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("제목 - " + i)
                            .content("내용 - " + i)
                            .build();
                })
                .collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //when
        // 웹 요청으로 정렬 조건을 넘길때
        mockMvc.perform(get("/posts?page=0&size=10")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(10)))
                .andExpect(jsonPath("$[0].id").value(63))
                .andExpect(jsonPath("$[0].title").value(requestPosts.get(29).getTitle()))
                .andExpect(jsonPath("$[0].content").value(requestPosts.get(29).getContent()))
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test7() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit  editPost = PostEdit.builder()
                .title("수정한 제목")
                .content("내용")
                .build();

        //when
        // 웹 요청으로 정렬 조건을 넘길때
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editPost)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 제목 수정")
    void test8() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit  editPost = PostEdit.builder()
                .title(null)
                .content("수정한 내용")
                .build();

        //when
        // 웹 요청으로 정렬 조건을 넘길때
        mockMvc.perform(patch("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editPost)))
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.content").value("수정한 내용"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("글 삭제")
    void test9() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목 입니다.")
                .content("내용 입니다.")
                .build();
        postRepository.save(post);

        //expect
        mockMvc.perform(delete("/posts/{postId}", post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회")
    void test10() throws Exception {
        mockMvc.perform(get("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("존재하지 않는 게시글 수정")
    void test11() throws Exception {
        //given
        PostEdit editPost = PostEdit.builder()
                .title("제목 수정")
                .content("내용 수정")
                .build();

        //expected
        mockMvc.perform(patch("/posts/{postId}", 1L)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(editPost)))
                .andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 제목에 잘못된 글자 포함시 검증")
    void test12() throws Exception {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("씨발")
                .content("내용")
                .build();

        mockMvc.perform(post("/posts")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreate)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }


}