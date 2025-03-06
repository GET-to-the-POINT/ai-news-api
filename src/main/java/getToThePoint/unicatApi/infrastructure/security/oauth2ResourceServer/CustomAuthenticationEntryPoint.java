package getToThePoint.unicatApi.infrastructure.security.oauth2ResourceServer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import getToThePoint.unicatApi.common.util.JwtUtil;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final JwtUtil jwtUtil;
    private final MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        jwtUtil.removeJwtCookie(response);
        String errorMessage = messageSource.getMessage("error.jwt.invalid", null, "", request.getLocale());
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMessage);
    }
}