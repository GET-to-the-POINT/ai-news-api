package getToThePoint.unicatApi.infrastructure.security.oAuth2Client.oAuth2User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    @Setter
    @Getter
    private Long memberId;

    @Override
    public String getId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getEmail() {
        return Optional.ofNullable((Map<String, Object>) attributes.get("kakao_account"))
                .map(kakaoAccount -> (String) kakaoAccount.get("email"))
                .orElseThrow(() -> new IllegalStateException("Kakao email is missing!"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getName() {
        return Optional.ofNullable((Map<String, Object>) attributes.get("kakao_account"))
                .map(kakaoAccount -> (Map<String, Object>) kakaoAccount.get("profile"))
                .map(profile -> (String) profile.get("nickname"))
                .orElseThrow(() -> new IllegalStateException("Kakao name is missing!"));
    }

    @Override
    @SuppressWarnings("unchecked")
    public String getPicture() {
        return Optional.ofNullable((Map<String, Object>) attributes.get("kakao_account"))
                .map(kakaoAccount -> (Map<String, Object>) kakaoAccount.get("profile"))
                .map(profile -> (String) profile.get("profile_image_url"))
                .orElse("");
    }

    @Override
    public Map<String, Object> getAttributes() {
        return Map.of(
                "id", getId(),
                "email", getEmail(),
                "name", getName(),
                "picture", getPicture(),
                "memberId", getMemberId()
        );
    }
}