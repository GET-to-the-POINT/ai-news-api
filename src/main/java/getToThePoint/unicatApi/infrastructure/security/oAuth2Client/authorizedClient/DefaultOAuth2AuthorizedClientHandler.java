package getToThePoint.unicatApi.infrastructure.security.oAuth2Client.authorizedClient;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;

public class DefaultOAuth2AuthorizedClientHandler implements VendorOAuth2AuthorizedClientHandler {
    @Override
    public OAuth2AuthorizedClient handle(OAuth2AuthorizedClient newClient, OAuth2AuthorizedClient existingClient) {
        return newClient;
    }
}