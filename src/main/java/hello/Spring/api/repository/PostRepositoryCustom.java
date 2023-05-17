package hello.Spring.api.repository;

import hello.Spring.api.domain.Post;
import hello.Spring.api.request.PostSearch;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getList(PostSearch postSearch);
}
