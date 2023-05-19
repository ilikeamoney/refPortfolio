package hello.Spring.api.exception;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class SuperException extends RuntimeException {

    private final Map<String, String> validate = new HashMap<>();

    public SuperException(String message) {
        super(message);
    }

    public SuperException(String message, Throwable cause) {
        super(message, cause);
    }


    // 에러 코드 추상화
    public abstract int getStatusCode();

    public void addValidation(String filedName, String message) {
        // 상위 예외 클래스에서 초기화된 Map 에 값이 담긴다.
        validate.put(filedName, message);
    };
}
