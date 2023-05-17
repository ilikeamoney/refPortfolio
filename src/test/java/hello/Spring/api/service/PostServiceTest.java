package hello.Spring.api.service;

import hello.Spring.api.domain.Post;
import hello.Spring.api.repository.PostRepository;
import hello.Spring.api.response.PostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("글 작성")
    public void test1() throws Exception {
        //given
        Post postCreate = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        Post savePost = postRepository.save(postCreate);

        //then
        Assertions.assertThat(postRepository.count()).isEqualTo(savePost.getId());
        Post post = postRepository.findAll().get(0);
        assertThat(postRepository.count()).isEqualTo(1);
        assertThat(post.getTitle()).isEqualTo("제목입니다.");
        assertThat(post.getContent()).isEqualTo("내용입니다.");
    }


    @Test
    @DisplayName("글 한개 조회")
    public void test2() throws Exception {
        //given
        Post requestPost = Post.builder()
                .title("foo")
                .content("bar")
                .build();

        Post savePost = postRepository.save(requestPost);

        //when
        PostResponse post = postService.get(savePost.getId());

        //then
        Assertions.assertThat(post).isNotNull();
        Assertions.assertThat(post.getTitle()).isEqualTo("foo");
        Assertions.assertThat(post.getContent()).isEqualTo("bar");
    }

    @Test
    @DisplayName("글 여러개 조회")
    public void test3() throws Exception {
        //given

        postRepository.saveAll(
                List.of(
                Post.builder()
                        .title("foo1")
                        .content("bar1")
                        .build(),

                Post.builder()
                        .title("foo2")
                        .content("bar2")
                        .build())
        );

        //when
        List<PostResponse> posts = postService.getList();

        //then
        Assertions.assertThat(posts.size()).isEqualTo(2);
    }
}