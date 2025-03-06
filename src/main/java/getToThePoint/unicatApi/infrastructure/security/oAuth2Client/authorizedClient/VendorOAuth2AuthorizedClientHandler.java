package getToThePoint.unicatApi.infrastructure.security.oAuth2Client.authorizedClient;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public interface VendorOAuth2AuthorizedClientHandler {

    /**
     * 새로 전달된 authorizedClient와 기존 저장된 authorizedClient가 있을 경우
     * 각 벤더 정책에 따라 refresh token(및 메타데이터)을 어떻게 처리할지 결정합니다.
     *
     * @param newClient      새로 전달된 OAuth2AuthorizedClient
     * @param existingClient 기존에 저장된 OAuth2AuthorizedClient (없으면 null)
     * @return 최종적으로 저장할 OAuth2AuthorizedClient
     */
    OAuth2AuthorizedClient handle(OAuth2AuthorizedClient newClient, OAuth2AuthorizedClient existingClient);

    static VendorOAuth2AuthorizedClientHandler getHandler(String registrationId) {
        return switch (registrationId.toLowerCase()) {
            case "google" -> new GoogleOAuth2AuthorizedClientHandler();
            case "kakao" -> new KakaoOAuth2AuthorizedClientHandler();
            default -> new DefaultOAuth2AuthorizedClientHandler();
        };
    }
}