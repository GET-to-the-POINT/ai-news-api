package getToThePoint.unicatApi.infrastructure.security.oAuth2Client;

import getToThePoint.unicatApi.infrastructure.security.oAuth2Client.authorizedClient.VendorOAuth2AuthorizedClientHandler;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

import static getToThePoint.unicatApi.infrastructure.security.oAuth2Client.authorizedClient.VendorOAuth2AuthorizedClientHandler.getHandler;

@Service
public class CustomJdbcOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private final JdbcOAuth2AuthorizedClientService delegate;

    public CustomJdbcOAuth2AuthorizedClientService(JdbcTemplate jdbcTemplate, ClientRegistrationRepository clientRegistrationRepository) {
        this.delegate = new JdbcOAuth2AuthorizedClientService(jdbcTemplate, clientRegistrationRepository);
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        OAuth2AuthorizedClient existingClient = loadAuthorizedClient(authorizedClient.getClientRegistration().getRegistrationId(), principal.getName());

        VendorOAuth2AuthorizedClientHandler handler = getHandler(authorizedClient.getClientRegistration().getRegistrationId());

        OAuth2AuthorizedClient clientToSave = handler.handle(authorizedClient, existingClient);
        delegate.saveAuthorizedClient(clientToSave, principal);
    }

    @Override
    public OAuth2AuthorizedClient loadAuthorizedClient(String clientRegistrationId, String principalName) {
        return delegate.loadAuthorizedClient(clientRegistrationId, principalName);
    }

    @Override
    public void removeAuthorizedClient(String clientRegistrationId, String principalName) {
        delegate.removeAuthorizedClient(clientRegistrationId, principalName);
    }
}