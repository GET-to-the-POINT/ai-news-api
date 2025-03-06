package getToThePoint.unicatApi.presentation.controller;

import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.RSAKey;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import getToThePoint.unicatApi.common.propertie.AppProperties;
import getToThePoint.unicatApi.domain.dto.oauth.OAuthLinkDto;

import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "Auth API", description = "Auth 관련 기능 API")
public class AuthController {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final AppProperties appProperties;
    private final RSAPublicKey publicKey;

    @GetMapping("/.well-known/jwks.json")
    @Operation(summary = "JWK Set 조회", description = "현재 RSA 공개키를 기반으로 생성된 JWK Set 정보를 반환합니다.", responses = {@ApiResponse(responseCode = "200", description = "JWK Set 조회 성공", content = @Content(mediaType = "application/json", examples = {@ExampleObject(name = "JWK Set", summary = "예시 JWK Set", value = """
            {
              "keys": [
                {
                  "kty": "RSA",
                  "e": "AQAB",
                  "use": "sig",
                  "kid": "rsa-key-id",
                  "alg": "RS256",
                  "n": "1n4iFl-u0JR0Fdm62KFkaRZv-i6o3fBGyPpwpuoNwP84hEDgwfuDlgN9S6Hqaz_GNDxZqRlAdzOFz4DRJQOo_fPh..."
                }
              ]
            }
            """)}))})
    public Map<String, Object> getJwks() {
        JWK jwk = new RSAKey.Builder(publicKey).keyID(appProperties.jwt().keyId()).algorithm(JWSAlgorithm.RS256).keyUse(KeyUse.SIGNATURE).build();

        return new JWKSet(jwk).toJSONObject();
    }

    @GetMapping("/oauth-links")
    @Operation(summary = "OAuth 인증 링크 목록 조회", description = "OAuth 인증에 필요한 각 공급자별 링크 목록을 반환합니다.", responses = {@ApiResponse(responseCode = "200", description = "성공적으로 OAuth 링크 목록을 조회함", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OAuthLinkDto.class))))})
    public List<OAuthLinkDto> getOAuthHrefLinks() {
        List<OAuthLinkDto> oAuthLinkDtos = new ArrayList<>();

        Iterable<ClientRegistration> registrations = ((InMemoryClientRegistrationRepository) clientRegistrationRepository);

        String baseUrl = appProperties.api().protocol() + "://" + appProperties.api().domain() + ":" + appProperties.api().port();
        registrations.forEach(registration -> {
            String href = "/oauth2/authorization/" + registration.getRegistrationId();
            OAuthLinkDto oAuthLinkDto = OAuthLinkDto.builder().provider(registration.getClientName()).link(baseUrl + href).build();
            oAuthLinkDtos.add(oAuthLinkDto);
        });
        return oAuthLinkDtos;
    }

    @PostMapping("/token")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "토큰 리프레시", description = "쿠키에 담긴 토큰을 직접 리프레시 합니다. 모든 요청에 토큰이 리프레시 되기 때문에 별도의 요청이 필요하지 않습니다.")
    @ApiResponse(responseCode = "204", description = "토큰이 성공적으로 갱신되었으나, 응답 본문은 없습니다.")
    public void refreshToken() {
        // 필터 체이닝에서 토큰을 갱신하기 때문에 여기서는 엔드포인트만 만들어준다.
    }
}