package hello.Spring.api.exception;

public class InvalidSignInformation extends SuperException {

    private static final String MESSAGE = "아이디/비밀번호가 올바르지 않습니다.";

    public InvalidSignInformation() {
        super(MESSAGE);
    }


    @Override
    public int getStatusCode() {
        return 400;
    }
}
