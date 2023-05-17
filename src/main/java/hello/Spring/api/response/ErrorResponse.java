package hello.Spring.api.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
 */
@Getter
@Setter
@Builder
public class ErrorResponse {

    private final String code;

    private final String message;

    private final Map<String, String> validation = new HashMap<>();

    @Builder
    public ErrorResponse(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public void addValidation(String fieldName, String errorMessage) {
        this.validation.put(fieldName, errorMessage);
    }
}
