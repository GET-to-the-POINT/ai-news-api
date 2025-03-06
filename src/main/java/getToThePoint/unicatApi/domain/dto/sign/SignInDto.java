package getToThePoint.unicatApi.domain.dto.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Schema(description = "사용자 사인인을 위한 데이터 DTO")
@Builder
@Jacksonized
public record SignInDto(
        @NotBlank(message = "{email.required}")
        @Schema(description = "사용자 이메일", example = "user@example.com")
        String email,

        @NotBlank(message = "{password.required}")
        @Schema(description = "사용자 비밀번호", example = "StrongPassword123")
        String password
) {}