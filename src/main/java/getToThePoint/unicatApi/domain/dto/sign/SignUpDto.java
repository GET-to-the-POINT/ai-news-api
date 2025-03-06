package getToThePoint.unicatApi.domain.dto.sign;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

@Schema(description = "사용자 회원가입에 필요한 데이터 DTO")
@Builder
@Jacksonized
public record SignUpDto(
        @NotBlank(message = "{email.required}")
        @Email(message = "{email.valid}")
        @Schema(description = "사용자 이메일", example = "user@example.com")
        String email,

        @NotBlank(message = "{password.required}")
        @Schema(description = "사용자 비밀번호", example = "StrongPassword123")
        String password
) {}