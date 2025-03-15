package gettothepoint.unicatapi.domain.dto.project;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ScriptRequest(@NotBlank @Size(min = 20, message = "20자 이상을 입력하세요.") String script) {
}
