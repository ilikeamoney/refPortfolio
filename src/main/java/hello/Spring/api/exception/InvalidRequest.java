package hello.Spring.api.exception;

import lombok.Getter;

/**
 * status -> 400
 */
@Getter
public class InvalidRequest extends SuperException {

    private static final String MESSAGE = "현재 포함함 수 없는 단어가 있습니다.";

    public String filedName;
    public String message;

    public InvalidRequest() {
        super(MESSAGE);
    }

    public InvalidRequest(Throwable cause) {
        super(MESSAGE, cause);
    }

    public InvalidRequest(String filedName, String message) {
        super(MESSAGE);
        this.filedName = filedName;
        this.message = message;
        // 예외가 발생한 곳에서 넘겨준 filedName, message 가 넘어온다.
        addValidation(filedName, message);
    }


    // 에러코드 하위 타입에서 정의
    @Override
    public int getStatusCode() {
        return 400;
    }
}
