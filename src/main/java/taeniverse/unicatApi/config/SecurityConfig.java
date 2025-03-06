package taeniverse.unicatApi.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import taeniverse.unicatApi.component.filter.ReissueAccessTokenFilter;
import taeniverse.unicatApi.component.oauth2.CustomAuthenticationEntryPoint;
import taeniverse.unicatApi.component.oauth2.CustomOAuth2AuthenticationSuccessHandler;
import taeniverse.unicatApi.component.oauth2.CustomOAuth2AuthorizationRequestResolver;
import taeniverse.unicatApi.component.oauth2.MultiBearerTokenResolver;
import taeniverse.unicatApi.mvc.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final CustomOAuth2AuthenticationSuccessHandler oauth2SuccessHandler;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final MultiBearerTokenResolver multiBearerTokenResolver;
    private final ReissueAccessTokenFilter reissueAccessTokenFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final JwtAuthenticationConverter jwtAuthenticationConverter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/error").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .requestMatchers(HttpMethod.GET, "/.well-known/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/sign-in", "/sign-up").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/sign-out").permitAll()
                        .requestMatchers(HttpMethod.GET, "/oauth-links").permitAll()

                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
//                        .authorizationEndpoint(authorizationEndpoint -> authorizationEndpoint
//                                .authorizationRequestResolver(new CustomOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization"))
//                        )
                        .userInfoEndpoint(userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService)
                        )
                        .successHandler(oauth2SuccessHandler)
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .bearerTokenResolver(multiBearerTokenResolver)
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                )
                .addFilterAfter(reissueAccessTokenFilter, AuthorizationFilter.class);

        return http.build();
    }
}
