package hello.Spring.api.service;

import hello.Spring.api.domain.Post;
import hello.Spring.api.exception.PostNotFound;
import hello.Spring.api.repository.PostRepository;
import hello.Spring.api.request.PostEdit;
import hello.Spring.api.request.PostSearch;
import hello.Spring.api.response.PostResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @BeforeEach
    void deletePost() {
        postRepository.deleteAll();
    }

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
        postRepository.save(postCreate);

        //then
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

    @Test
    @DisplayName("글 제목 수정")
    public void test5() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit editPost = PostEdit.builder()
                .title("수정한 제목")
                .build();

        postService.edit(post.getId(), editPost);

        //when
        Post findPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다."));

        //then
        Assertions.assertThat(findPost.getTitle()).isEqualTo("수정한 제목");
    }

    @Test
    @DisplayName("글 내용 수정")
    public void test6() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit editPost = PostEdit.builder()
                .content("수정한 내용")
                .build();

        postService.edit(post.getId(), editPost);

        //when
        Post findPost = postRepository.findById(post.getId())
                .orElseThrow(() -> new RuntimeException("글이 존재하지 않습니다."));

        //then
        Assertions.assertThat(findPost.getContent()).isEqualTo("수정한 내용");
    }


    @Test
    @DisplayName("글 삭제")
    void test7() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목 입니다.")
                .content("내용 입니다.")
                .build();
        postRepository.save(post);

        //when
        postService.delete(post.getId());

        //then
        assertThatThrownBy(() -> postService.get(post.getId()))
                .isInstanceOf(PostNotFound.class);

        assertThat(postRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("글 한개 조회 - 존재하지 않는 글")
    public void test8() throws Exception {
        //given
        Post requestPost = Post.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        postRepository.save(requestPost);

        //expected
        Assertions.assertThatThrownBy(() -> postService.get(requestPost.getId() + 1L))
                .isInstanceOf(PostNotFound.class);
    }

    @Test
    @DisplayName("글 삭제 - 존재하지 않는 글")
    void test9() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목 입니다.")
                .content("내용 입니다.")
                .build();
        postRepository.save(post);

        //expected
        assertThatThrownBy(() -> postService.delete(post.getId() + 1L))
                .isInstanceOf(PostNotFound.class);
    }

    @Test
    @DisplayName("글 내용 수정 - 존재하지 않는 글")
    public void test10() throws Exception {
        //given
        Post post = Post.builder()
                .title("제목")
                .content("내용")
                .build();
        postRepository.save(post);

        PostEdit editPost = PostEdit.builder()
                .content("수정한 내용")
                .build();

        //expected
        assertThatThrownBy(() -> postService.edit(post.getId() + 1L, editPost))
                .isInstanceOf(PostNotFound.class);
    }
}