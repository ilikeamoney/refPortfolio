package hello.Spring.api.config;

import hello.Spring.api.domain.Member;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

public class MemberPrincipal extends User {

    private final Long memberId;

    public MemberPrincipal(Member member) {
        super(member.getEmail(), member.getPassword(),
                List.of(
                        new SimpleGrantedAuthority("ROLE_USER"))
//                        new SimpleGrantedAuthority("WRITE")
        );

        this.memberId = member.getId();
    }

    public Long getMemberId() {
        return memberId;
    }
}
