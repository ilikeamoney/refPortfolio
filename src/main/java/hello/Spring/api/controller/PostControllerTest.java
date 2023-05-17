package hello.Spring.api.controller;

import hello.Spring.api.request.PostCreate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SSR -> thymeleaf, jsp, mustache, freemarker (Server Side Rendering)
 *          -> HTML rendering
 * SPA ->
 *  vue, nuxt
 *  react, next
 *  (Single Page Application)
 *      -> javascript <-> API (Json)
 */

@Slf4j
@RestController
public class PostControllerTest {

    // @RequestParam 사용 (기본적인 방법)
    @PostMapping("/posts/v0")
    public String post(@RequestParam String title, @RequestParam String content) {
        log.info("title = {}, content = {}", title, content);
        return "Hello World !";
    }

    // Map 으로도 넘겨주는거 가능
    @PostMapping("/posts/v1")
    public String postV1(@RequestParam Map<String, String> params) {
        log.info("params = {}", params);
        return "Hello World !";
    }

    // DTO 를 파리미터로 넘겨서 값을 바로 바인딩 가능
    // 하지만 Setter, Getter 있어야 값이 정상적으로 바인딩 가능하다.
    @PostMapping("/posts/v2")
    public String postV2(PostCreate params) {
        log.info("params = {}", params);
        return "Hello World !";
    }

    // HTTP 요청으로 넘어온 값을 바로 DTO 에 바인딩 해준다.
    @PostMapping("/posts/v3")
    public String postV3(@RequestBody PostCreate params) {
        log.info("params = {}", params);
        return "Hello World !";
    }

    @PostMapping("/posts/v4")
    public Map<String, String> postV4(@RequestBody @Valid PostCreate params, BindingResult result) {
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFiledError = fieldErrors.get(0);
            String filedName = firstFiledError.getField(); // filed name (key)
            String errorMessage = firstFiledError.getDefaultMessage(); // error message (value)

            Map<String, String> errorMap = new HashMap<>();
            errorMap.put(filedName, errorMessage);
            return errorMap;
        }
        log.info("params = {}", params);
        return Map.of();
    }
}
