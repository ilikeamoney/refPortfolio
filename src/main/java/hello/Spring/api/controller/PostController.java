package hello.Spring.api.controller;

import hello.Spring.api.request.PostCreate;
import hello.Spring.api.request.PostEdit;
import hello.Spring.api.request.PostSearch;
import hello.Spring.api.response.PostResponse;
import hello.Spring.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * API 문서 생성
 *
 * 한건 조회 -> GET /posts/{postId}
 * 게시글 등록 -> POST /posts
 * 게시글 목록 조회 -> GET /posts
 * 게시글 수정 -> PATCH /posts/{postId}
 * 게시글 삭제 -> DELETE /posts/{postId}
 *
 * RestDoc 장점
 * 운영코드에 영향 X
 * 코드를 수정하고 테스트 케이스에서 통과가 되면 RestDoc 는 테스트 기반으로 문서를 수정한다.
 * (즉 코드수정을 하면 문서도 수정을 해야 스펙이 일치하지만 RestDoc 는 코드를 수정하면 문서가 테스트 기반으로 자동 수정된다.)
 *
 */


@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) {
        request.validate();
        postService.write(request);
    }

    @GetMapping("/posts/{postId}")
    public PostResponse get(@PathVariable Long postId) {
        return postService.get(postId);
    }

    @GetMapping("/posts")
    public List<PostResponse> getList(@ModelAttribute PostSearch postSearch) {
        return postService.getList(postSearch);
    }

    @PatchMapping("/posts/{postId}")
    public PostResponse edit(@PathVariable Long postId, @RequestBody PostEdit postEdit) {
        return postService.edit(postId, postEdit);
    }

    @DeleteMapping("/posts/{postId}")
    public void delete(@PathVariable Long postId) {
        postService.delete(postId);
    }

}
