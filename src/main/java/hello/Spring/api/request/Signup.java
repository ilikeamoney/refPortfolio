package hello.Spring.api.request;

import lombok.Builder;
import lombok.Data;

@Data
public class Signup {

    private String password;

    private String name;

    private String email;


    @Builder
    public Signup(String password, String name, String email) {
        this.password = password;
        this.name = name;
        this.email = email;
    }
}
