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
@Schema(description = "필드 오류 상세 모델")
public class FieldError {

    @Schema(description = "오브젝트 이름", example = "signInDto")
    private String objectName;

    @Schema(description = "필드 이름", example = "password")
    private String field;

    @Schema(description = "거부된 값")
    private Object rejectedValue;

    @Schema(description = "오류 코드 리스트", example = "[\"NotBlank.signInDto.password\", \"NotBlank.password\"]")
    private List<String> codes;

    @Schema(description = "기본 오류 메시지", example = "비밀번호는 필수 입력사항입니다.")
    private String defaultMessage;

    @Schema(description = "바인딩 실패 여부", example = "false")
    private boolean bindingFailure;

    @Schema(description = "오류 코드", example = "NotBlank")
    private String code;
}