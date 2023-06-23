package hello.Spring.api.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Mvc 구성을 확장하고 싶은때는 WebMvcConfigurer 에 메소드를 오버라이딩 한다.
 *
 *
 * Interceptor 구성 추가하는 방법
 * 인증을 받을 경로는 addPathPatterns 에 원하는 경로를 추가하면 된다.
 * 인증을 제외할 라우터는 excludePathPatterns 에 원하는 경로를 추가하면 된다.
 * 하지만 프로젝트 규모가 커질시 제외해야하는 패턴의 수가 증가함
 * 그렇기 때문에 프로젝트 내에서 정한 규율은 인증이 필요한 Path 의 경우 DTO를 파라미터로 받는다고 프로젝트 내에 규율을 정한다.
 *
 * 인터셉터를 추가할때는  에 addInterceptor 를 오버라이딩 한다.
 *
 * ArgumentResolver 구성 추가하는 방법
 */

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

    private final AppConfig appConfig;
}
