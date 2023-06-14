package hello.Spring.api.exception;

/**
 * status = 401
 * 인증되지 않은 사용자에게 보여질 에러 Json
 */

public class Unauthorized extends SuperException {
    private static final String MESSAGE = "인증이 필요합니다.";

    public Unauthorized() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
