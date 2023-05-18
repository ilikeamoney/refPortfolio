package hello.Spring.api.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import hello.Spring.api.domain.Post;
import hello.Spring.api.request.PostSearch;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static hello.Spring.api.domain.QPost.post;

@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 페이징 처리 코드
     */
    @Override
    public List<Post> getList(PostSearch postSearch) {
        return queryFactory.selectFrom(post)
                .offset(postSearch.getOffset())
                .limit(postSearch.getSize())
                .orderBy(post.id.desc())
                .fetch();
    }
}
