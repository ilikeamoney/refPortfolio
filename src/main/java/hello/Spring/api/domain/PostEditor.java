package hello.Spring.api.domain;

import hello.Spring.api.request.PostEdit;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostEditor {

    private final String title;

    private final String content;

    public PostEditor(String title, String content) {
        this.title = title;
        this.content = content;
    }


    public static PostEditor.PostEditorBuilder callEditor(PostEdit postEdit) {
        return new PostEditorBuilder()
                .title(postEdit.getTitle())
                .content(postEdit.getContent());
    }

    public static class PostEditorBuilder {
        private String title;

        private String content;

        public PostEditorBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PostEditorBuilder content(String content) {
            this.content = content;
            return this;
        }

        public PostEditor build() {
            return new PostEditor(this.title, this.content);
        }

    }

}
