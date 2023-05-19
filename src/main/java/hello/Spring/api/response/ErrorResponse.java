package hello.Spring.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * 오류처리 Json 출력 예시
 *
 * {
 *     "code" : "400"
 *     "message" : "잘못된 요청입니다."
 *     "validation" : {
 *         "title" : "값을 입력해 주세요"
 *         "content" : "...."
 *     }
 * }
 *
 * @JsonInclude 응답 json 결과를 필터링하는 애노테이션
 * @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
 */
@Getter
public class ErrorResponse {

    private final String code;

    private final String message;

    private final Map<String, String> validation;

    @Builder
    public ErrorResponse(String code, String message, Map<String, String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation != null ? validation : new HashMap<>();
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
