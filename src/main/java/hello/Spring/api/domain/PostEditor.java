package hello.Spring.api.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {

    private final String title;

    private final String content;

    @Builder
    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }


//    public static PostEditor.PostEditorBuilder callEditor(PostEdit postEdit) {
//        return PostEditor.builder()
//                .title(postEdit.getTitle())
//                .content(postEdit.getContent());
//    }

}
