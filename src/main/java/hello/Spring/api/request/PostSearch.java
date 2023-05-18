package hello.Spring.api.request;

import lombok.Builder;
import lombok.Getter;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @Builder 를 클래스 레벨에 붙여서 사용하면
 * @Builder.Default 을 사용할 수 있다.
 * 필드에 고정값을 적용할때 사욤한다.
 */

@Getter
@Builder
public class PostSearch {

    private final int MAX_SIZE = 2000;

    @Builder.Default
    private int page = 1;
    @Builder.Default
    private int size = 10;

//    @Builder
//    public PostSearch(Integer page, Integer size) {
//        this.page = page;
//        this.size = size;
//    }


    /**
     * 페이징 처리 코드
     */
    public long getOffset() {
        return (long) (max(1, page) - 1) * min(size, MAX_SIZE);
    }
}
