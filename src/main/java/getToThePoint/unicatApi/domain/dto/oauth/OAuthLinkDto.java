package getToThePoint.unicatApi.domain.dto.oauth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Schema(description = "OAuth Link DTO")
@Builder
@Jacksonized
public record OAuthLinkDto(
        @Schema(description = "OAuth Provider", example = "Google") String provider,
        @Schema(description = "OAuth Link", example = "https://example.com/oauth2/authorization/google") String link) { }