package hello.Spring.api.config;

import hello.Spring.api.config.data.UserSession;
import hello.Spring.api.exception.Unauthorized;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * ArgumentResolver 확장해서 사용하는 방법
 *
 * HandlerMethodArgumentResolver 구현하여 두개의 메소드를 확장한다.
 *
 * supportsParameter 메소드는 Controller 메소드에 넘어온 파라미터 타입을 검증한다.
 */

public class AuthResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }


    /*
    Session 검증에서 데이터 베이스를 조회해서 사용자의 정보와 일지하는지
    검증하는 로직을 만들면 보안에 더 안정감을 준다.
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        String accessToken = webRequest.getHeader("Authorization");
        if (accessToken == null || accessToken.equals("")) {
            throw new Unauthorized();
        }

        // 데이터 베이스를 조회해서 사용자의 값을 Session DTO 에 생성자 파라미터로 넘겨서
        // 반환한다.

        return new UserSession(1L);
    }
}
