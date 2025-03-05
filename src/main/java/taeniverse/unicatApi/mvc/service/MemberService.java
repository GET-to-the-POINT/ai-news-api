package taeniverse.unicatApi.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import taeniverse.unicatApi.mvc.model.entity.Member;
import taeniverse.unicatApi.mvc.model.entity.Role;
import taeniverse.unicatApi.mvc.repository.MemberRepository;
import taeniverse.unicatApi.mvc.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "해당 이메일을 가진 사용자가 없습니다: " + email));
    }

    @Transactional
    public Member findOrCreateMember(String email) {
        return memberRepository.findByEmail(email).orElseGet(() -> {
            Member newMember = Member.builder()
                    .email(email)
                    .password("{noop}oauth2user")  // 추후 변경 가능
                    .build();
            memberRepository.save(newMember);

            // 기본 ROLE 부여
            Role role = Role.builder()
                    .name("USER")
                    .member(newMember)
                    .build();
            roleRepository.save(role);

            return newMember;
        });
    }
}