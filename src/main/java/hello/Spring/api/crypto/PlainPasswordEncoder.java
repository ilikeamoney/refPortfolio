package hello.Spring.api.crypto;

import hello.Spring.api.domain.Member;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile(value = "test")
@Component
public class PlainPasswordEncoder implements PasswordEncoder {

    @Override
    public String encrypt(String rawPassword) {
        return rawPassword;
    }

    @Override
    public boolean equalsPassword(String rawPassword, Member member) {
        return rawPassword.equals(member.getPassword());
    }
}
