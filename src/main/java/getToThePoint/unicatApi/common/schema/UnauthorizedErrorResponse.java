package getToThePoint.unicatApi.common.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "오류 응답 모델 (401 Unauthorized)")
public class UnauthorizedErrorResponse {

    @Schema(description = "타임스탬프", example = "2025-03-01T14:10:38.614+00:00")
    private String timestamp;

    @Schema(description = "HTTP 상태 코드", example = "401")
    private int status;

    @Schema(description = "오류 메시지", example = "Unauthorized")
    private String error;

    @Schema(description = "예외 클래스", example = "org.springframework.web.server.ResponseStatusException")
    private String exception;

    @Schema(description = "오류 메시지 상세", example = "잘못된 이메일 또는 비밀번호입니다")
    private String message;

    @Schema(description = "요청 경로", example = "/api/sign-in")
    private String path;
}