package hello.Spring.api.crypto;

import hello.Spring.api.domain.Member;

public interface PasswordEncoder {

    String encrypt(String rawPassword);

    boolean equalsPassword(String rawPassword, Member member);
}
