package taeniverse.unicatApi.component.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import taeniverse.unicatApi.component.util.JwtUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Long memberId = oAuth2User.getAttribute("memberId");
        String email = oAuth2User.getAttribute("email");
        List<String> roleNames = oAuth2User.getAttribute("roles");

        assert memberId != null;
        String token = jwtUtil.generateJwtToken(memberId, email, roleNames);
        jwtUtil.addJwtCookie(response, token);

        String state = request.getParameter("state");
        String redirect = "/";
        if (state != null && state.contains("|")) {
            String[] parts = state.split("\\|");
            if (parts.length == 2) {
                redirect = java.net.URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
            }
        }
        response.sendRedirect(redirect);
    }
}