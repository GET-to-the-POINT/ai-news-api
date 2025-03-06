package taeniverse.unicatApi.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import taeniverse.unicatApi.mvc.model.entity.Member;
import taeniverse.unicatApi.mvc.model.entity.OAuthLink;
import taeniverse.unicatApi.mvc.model.entity.Role;
import taeniverse.unicatApi.mvc.repository.MemberRepository;
import taeniverse.unicatApi.mvc.repository.OAuthLinkRepository;
import taeniverse.unicatApi.mvc.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final OAuthLinkRepository oAuthLinkRepository;

    public Member create(String email, String password) {
        Member member = Member.builder().email(email)
                .password(password)
                .build();

        return memberRepository.save(member);
    }

    public Member findOrCreateMember(String email, String registrationId) {
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

            // OAuthLink 생성
            OAuthLink oAuthLink = OAuthLink.builder()
                    .email(email)
                    .provider(registrationId)
                    .member(newMember)
                    .build();
            oAuthLinkRepository.save(oAuthLink);

            return newMember;
        });
    }
}