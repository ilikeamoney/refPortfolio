package hello.Spring.api.config;

import hello.Spring.api.exception.Unauthorized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 인터셉터를 확장할때는 HandlerInterceptor 를 구현한다.
 * preHandle 핸들러가 호출되기 전에
 * postHandle 핸들러가 호출될때
 * afterHandle 핸들러가 호출되고 나서
 *
 * 파라미터 요청에 검증을 구현하는 방법이다.
 */

@Slf4j
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(">> preHandle");
        String accessToken = request.getParameter("accessToken");
        if (accessToken != null && !accessToken.equals("")) {
            request.setAttribute("userName", accessToken);
            return true;
        }
        throw new Unauthorized();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }
}
