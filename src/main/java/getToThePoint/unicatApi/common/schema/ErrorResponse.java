package getToThePoint.unicatApi.common.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "오류 응답 모델 (400 Bad Request)")
public class ErrorResponse {

    @Schema(description = "타임스탬프", example = "2025-03-01T14:10:23.961+00:00")
    private String timestamp;

    @Schema(description = "HTTP 상태 코드", example = "400")
    private int status;

    @Schema(description = "오류 메시지", example = "Bad Request")
    private String error;

    @Schema(description = "예외 클래스", example = "org.springframework.web.bind.MethodArgumentNotValidException")
    private String exception;

    @Schema(description = "오류 메시지 상세", example = "Validation failed for object='signInDto'. Error count: 1")
    private String message;

    @Schema(description = "필드별 오류 상세 목록")
    private List<FieldError> errors;

    @Schema(description = "요청 경로", example = "/api/sign-in")
    private String path;
}
