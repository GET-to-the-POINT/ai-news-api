package taeniverse.unicatApi.mvc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import taeniverse.unicatApi.component.oauth2.OAuth2UserInfo;
import taeniverse.unicatApi.mvc.model.entity.Member;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberService memberService;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2UserInfo userInfo = OAuth2UserInfo.from(registrationId, attributes);
        String email = userInfo.getEmail();
        Member member = memberService.findOrCreateMember(email, registrationId);
        userInfo.setMemberId(member.getId());

        Collection<GrantedAuthority> authorities = userInfo.getAttributes().keySet().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new DefaultOAuth2User(authorities, userInfo.getAttributes(), "memberId");
    }
}