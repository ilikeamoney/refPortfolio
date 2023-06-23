package hello.Spring.api.crypto;

import hello.Spring.api.domain.Member;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = "local")
@Component
public class SCryptPasswordEncoder implements PasswordEncoder {

    private final org.springframework.security.crypto.scrypt.SCryptPasswordEncoder encoder = new org.springframework.security.crypto.scrypt.SCryptPasswordEncoder(16, 8, 1, 32, 64);


    public String encrypt(String password) {
        return encoder.encode(password);
    }

    public boolean equalsPassword(String rawPassword, Member member) {
        return encoder.matches(rawPassword, member.getPassword());
    }

}
