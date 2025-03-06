package getToThePoint.unicatApi.infrastructure.security.oAuth2Client;

import getToThePoint.unicatApi.application.service.MemberService;
import getToThePoint.unicatApi.domain.entity.Member;
import getToThePoint.unicatApi.infrastructure.security.oAuth2Client.oAuth2User.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

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

        Collection<GrantedAuthority> authorities = List.of(); // 석세스 핸들러가 사용하지 않음

        return new DefaultOAuth2User(authorities, userInfo.getAttributes(), "memberId");
    }
}