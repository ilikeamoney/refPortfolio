package hello.Spring.api.domain;

import hello.Spring.api.request.PostEdit;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Lob
    private String content;


    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void editPost(PostEditor postEditor) {
        title = postEditor.getTitle() != null ? postEditor.getTitle() : this.title;
        content = postEditor.getContent() != null ? postEditor.getContent() : this.content;
    }
}
