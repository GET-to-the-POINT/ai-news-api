package taeniverse.unicatApi.component.oauth2;

import java.util.Map;

public interface OAuth2UserInfo {
    String getId();
    String getEmail();
    String getPicture();
    String getName();
    Long getMemberId();
    void setMemberId(Long memberId);

    Map<String, Object> getAttributes();

    static OAuth2UserInfo from(String registrationId, Map<String, Object> attributes) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "kakao" -> new KakaoOAuth2UserInfo(attributes);
            default -> throw new IllegalArgumentException("Unsupported provider: " + registrationId);
        };
    }
}