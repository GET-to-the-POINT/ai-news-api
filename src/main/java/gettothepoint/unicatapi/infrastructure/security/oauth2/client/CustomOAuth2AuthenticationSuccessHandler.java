package gettothepoint.unicatapi.infrastructure.security.oauth2.client;

import gettothepoint.unicatapi.common.util.CookieUtil;
import gettothepoint.unicatapi.common.util.JwtUtil;
import gettothepoint.unicatapi.infrastructure.security.oauth2.client.authorizedclient.HttpCookieOAuth2AuthorizationRequestRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        Long memberId = oAuth2User.getAttribute("memberId");
        String email = oAuth2User.getAttribute("email");

        assert memberId != null;
        String token = jwtUtil.generateJwtToken(memberId, email);
        jwtUtil.addJwtCookie(response, token);

        String redirectUri = CookieUtil.getCookieValue(request, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI)
                .orElse("/");
        CookieUtil.deleteCookie(request, response, HttpCookieOAuth2AuthorizationRequestRepository.REDIRECT_URI);

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }
}