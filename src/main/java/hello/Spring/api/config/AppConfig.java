package hello.Spring.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

/**
 * 서버 내부에서 메타 데이터를 applicationProperties 나 yml 파일에서 사용할때 쓰는 방법
 */

//@Configuration
@Data
@ConfigurationProperties(prefix = "zzabjong")
public class AppConfig {

    private byte[] jwtKey;


    // 지금 이 부분이 필드 값이 셋팅되지 않아서 토큰값을 받지 못하고 있음
    public void setJwtKey(String jwtKey) {
        this.jwtKey = Base64.getDecoder().decode(jwtKey);
    }

    public byte[] getJwtKey() {
        return jwtKey;
    }
}
