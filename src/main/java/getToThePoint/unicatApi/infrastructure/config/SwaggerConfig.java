package getToThePoint.unicatApi.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        String jwtSchemeName = "bearerAuth";

        String description = """
            # 유니캣 API에 오신 것을 환영합니다. 🐱
            
            ## 사용법
            Oauth2를 이용한 **간편 인증 방법**과 **직접 정보를 기입하는 방식**이 있습니다.

            ### Oauth2 간편 인증 방법
            1. **[Oauth2 인증](https://api.unicat.day/login)**
            3. 인증 성공

            ### 직접 정보 입력 방식
            1. **[회원가입](#/Sign%20API/signUpForm_1)** 에 정보를 입력합니다.
            2. **[로그인](#/Sign%20API/signInForm_1)** 에 정보를 입력합니다.
            3. 인증 성공
            > 자세한 내용은 **[Sign API](#/Sign%20API)** 를 참조해주세요.

            ## 시스템 특징
            - 쿠키 기반 JWT를 사용하여 인증을 처리합니다.
            
            ## 이슈 보고
            [GitHub 이슈](https://github.com/GET-to-the-POINT/unicat-api/issues)에 이슈를 등록해주세요.
            
            형식은 없으며 자유롭게 작성해주시면 됩니다.
            """;

        return new OpenAPI()
                .info(new Info().title("unicat-api").description(description))
                .addSecurityItem(new SecurityRequirement()
                        .addList(jwtSchemeName))
                .components(new Components()
                        .addSecuritySchemes(jwtSchemeName,
                                new SecurityScheme()
                                        .name(jwtSchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}