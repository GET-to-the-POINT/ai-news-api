package taeniverse.unicatApi.mvc.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.JdbcOAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomJdbcOAuth2AuthorizedClientService implements OAuth2AuthorizedClientService {

    private final JdbcOAuth2AuthorizedClientService delegate;

    public CustomJdbcOAuth2AuthorizedClientService(JdbcTemplate jdbcTemplate,
                                                   ClientRegistrationRepository clientRegistrationRepository) {
        this.delegate = new JdbcOAuth2AuthorizedClientService(jdbcTemplate, clientRegistrationRepository);
    }

    @Override
    public void saveAuthorizedClient(OAuth2AuthorizedClient authorizedClient, Authentication principal) {
        // 1) 기존에 저장된 Client 조회
        OAuth2AuthorizedClient existingClient = loadAuthorizedClient(
                authorizedClient.getClientRegistration().getRegistrationId(),
                principal.getName()
        );

        // 2) 새로 받은 authorizedClient에 refresh token이 없고, 기존 Client에는 refresh token이 있는 경우
        if (existingClient != null
                && authorizedClient.getRefreshToken() == null
                && existingClient.getRefreshToken() != null) {

            // 기존 refresh token을 그대로 재사용하기 위해 새 authorizedClient를 만듦
            OAuth2AuthorizedClient updatedClient = new OAuth2AuthorizedClient(
                    authorizedClient.getClientRegistration(),
                    authorizedClient.getPrincipalName(),
                    authorizedClient.getAccessToken(),
                    existingClient.getRefreshToken()  // 여기서 기존 refresh token을 유지
            );

            // 교체된 updatedClient를 저장
            delegate.saveAuthorizedClient(updatedClient, principal);
        } else {
            // 보통의 경우: 원본 authorizedClient를 저장
            delegate.saveAuthorizedClient(authorizedClient, principal);
        }
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