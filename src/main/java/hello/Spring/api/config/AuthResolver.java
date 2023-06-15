package hello.Spring.api.config;

import hello.Spring.api.config.data.UserSession;
import hello.Spring.api.domain.Session;
import hello.Spring.api.exception.Unauthorized;
import hello.Spring.api.repository.SessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Base64;

/**
 * ArgumentResolver 확장해서 사용하는 방법
 * <p>
 * HandlerMethodArgumentResolver 구현하여 두개의 메소드를 확장한다.
 * <p>
 * supportsParameter 메소드는 Controller 메소드에 넘어온 파라미터 타입을 검증한다.
 */

@Slf4j
@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {

    private final SessionRepository sessionRepository;

    private final AppConfig appConfig;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }


    // 암호화된 JWT 토큰값을 원래대로 되돌리기
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        log.info(">>> appConfig = {}", appConfig.toString());

        String jws = webRequest.getHeader("Authorization");
        if (jws == null || jws.equals("")) {
            throw new Unauthorized();
        }

        // 문자열 키 값을 바이트로 변환
        // Base64 는 String -> Byte, Byte -> String 으로 바꿔주는 라이브러리 이다.
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(appConfig.getJwtKey())
                    .build()
                    .parseClaimsJws(jws);

            String memberId = claimsJws.getBody().getSubject();
            return new UserSession(Long.parseLong(memberId));

        } catch (JwtException e) {
            throw new Unauthorized();
        }
    }

    /*
    쿠키 사용
    Session 검증에서 데이터 베이스를 조회해서 사용자의 정보와 일지하는지
    검증하는 로직을 만들면 보안에 더 안정감을 준다.
     */
//    @Override
//    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
//        if (servletRequest == null) {
//            log.error("servletRequest null");
//            throw new Unauthorized();
//        }
//
//        Cookie[] cookies = servletRequest.getCookies();
//        if (cookies.length == 0) {
//            log.error("Cookie null");
//            throw new Unauthorized();
//        }
//
//        String accessToken = cookies[0].getValue();
//
//        // 데이터 베이스를 조회해서 사용자의 값을 Session DTO 에 생성자 파라미터로 넘겨서 반환한다.
//        Session session = sessionRepository.findByAccessToken(accessToken)
//                .orElseThrow(Unauthorized::new);
//
//        // 실제로 토큰값을 넘겨주는 것이 아니라 맴버와 토큰의 연관관계 매핑으로 인해서
//        // 세션정보에 담겨있는 멤버의 아이디를 넘겨준다.
//        return new UserSession(session.getMember().getId());
//    }
}
