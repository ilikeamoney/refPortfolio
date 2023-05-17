package hello.Spring.api.service;

import hello.Spring.api.domain.Post;
import hello.Spring.api.repository.PostRepository;
import hello.Spring.api.request.PostSearch;
import hello.Spring.api.response.PostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    private static Post apply(int i) {
        return Post.builder()
                .title("제목 - " + i)
                .content("내용 - " + i)
                .build();
    }

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
    @DisplayName("1 페이지 조회")
    public void test4() throws Exception {
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

        PostSearch pageable = PostSearch.builder()
                .build();

        //when
        List<PostResponse> posts = postService.getList(pageable);

        //then
        assertThat(posts.size()).isEqualTo(10);
        assertThat(posts.get(0).getContent()).isEqualTo("내용 - 29");
        assertThat(posts.get(9).getContent()).isEqualTo("내용 - 20");
    }
}