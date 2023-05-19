package hello.Spring.api.controller;

import hello.Spring.api.exception.SuperException;
import hello.Spring.api.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @ControllerAdivce 애노테이션을 사용하면 Controller 에서
 * 예외가 발생시 ControllerAdvice 에 전달된다.
 * 발생한 에러의 이름을 정확히 알고있다면 @ExceptionHandler 에노테이션 value 에 기재하고
 * 파라미터에도 기재하면 그 예외처리를 담당하는 전용 컨트롤러가 만들어 진다.
 */

@Slf4j
@ControllerAdvice
public class ExceptionController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ErrorResponse exceptionHandler(MethodArgumentNotValidException e) {
//            FieldError fieldError = e.getFieldError();
//            String field = fieldError.getField();
//            String defaultMessage = fieldError.getDefaultMessage();

        ErrorResponse response = ErrorResponse.builder()
                .code("400")
                .message("잘못된 요청입니다")
                .build();

        // error field, default message 를 가져오는 방법
        for (FieldError fieldError : e.getFieldErrors()) {
            response.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return response;
    }

    @ResponseBody
    @ExceptionHandler(SuperException.class)
    public ResponseEntity<ErrorResponse> postNotFound(SuperException e) {
        int statusCode = e.getStatusCode();

        ErrorResponse responseBody = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(e.getMessage())
                // 셋팅된 Map 이 ErrorResponse 에 필드에 셋팅된다.
                .validation(e.getValidate())
                .build();

        // 추상화한 예외의 타입을 세밀하게 검증해서 에러 메세지 추가


        ResponseEntity<ErrorResponse> response = ResponseEntity.status(statusCode)
                .body(responseBody);

        return response;
    }

}
