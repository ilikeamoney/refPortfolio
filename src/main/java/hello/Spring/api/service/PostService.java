package hello.Spring.api.service;

import hello.Spring.api.domain.Post;
import hello.Spring.api.domain.PostEditor;
import hello.Spring.api.repository.PostRepository;
import hello.Spring.api.request.PostCreate;
import hello.Spring.api.request.PostEdit;
import hello.Spring.api.request.PostSearch;
import hello.Spring.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;

    public void write(PostCreate postCreate) {
        Post post = Post.builder()
                .title(postCreate.getTitle())
                .content(postCreate.getContent())
                .build();

        postRepository.save(post);
    }

    public PostResponse get(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글 아이디입니다."));

        return PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();
    }

    public List<PostResponse> getList(PostSearch postSearch) {
        // 페이징 정렬조건 추가하는 방법
//        Pageable pageable = PageRequest.of(page, 5, Sort.by(Sort.Direction.DESC, "id"));

        return postRepository.getList(postSearch).stream()
                .map(PostResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public PostResponse edit(Long postId, PostEdit postEdit) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글 입니다."));

        // 아직 빌드되지 않은 PostEditor 를 반환한다. (현재 Post Entity 의 내용을 담음)
        PostEditor.PostEditorBuilder toEdit = post.toEditor();

        // 파라미터로 넘어온 postEdit(수정 내용을 담은 DTO)
        // PostEditor 내용을 변경
        PostEditor editPost = toEdit
                .title(postEdit.getTitle()) // null
                .content(postEdit.getContent()) // null
                .build();

        // 실제 엔티티의 내용을 변경
        post.updatePost(editPost);
        return new PostResponse(post);
    }

    @Transactional
    public void delete(Long postId) {
        Post findPost = postRepository
                .findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("찾으시는 게시글이 없습니다."));

        postRepository.delete(findPost);
    }

}
