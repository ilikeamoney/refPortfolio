package hello.Spring.api.request;

import hello.Spring.api.exception.InvalidRequest;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * 서비스 정책에 맞는 요청 클래스를 사용
 */

@Getter
public class PostCreate {

    /*
    default message 는
    검증 조건의 애노테이션에서 변경할 수 있다.
     */
    @NotBlank(message = "타이틀을 입력해주세요")
    private String title;

    @NotBlank(message = "컨텐트를 입력해주세요")
    private String content;


    // ObjectMapper 를 사용해서 Json 으로 변환할때
    // 기본 생성자가 필요하다.
    public PostCreate() {
    }

    @Builder
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }


    // 요청 자체에서 검증하는 코드
    public void validate() {
        if (title.contains("씨발")) {
            throw new InvalidRequest("title", "씨발은 제목으로 사용할 수 없습니다.");
        }
    }
}
