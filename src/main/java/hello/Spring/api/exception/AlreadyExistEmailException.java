package hello.Spring.api.exception;

public class AlreadyExistEmailException extends SuperException {

    private static final String MESSAGE = "이미 존재하는 이메일 입니다.";

    public AlreadyExistEmailException() {
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
