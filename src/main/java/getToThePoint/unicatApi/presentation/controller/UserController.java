package taeniverse.unicatApi.mvc.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import taeniverse.unicatApi.mvc.service.UserService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/me/oauth2-linked")
    public List<String> getOAuth2LinkedUser(@AuthenticationPrincipal Jwt jwt) {

        Long memberId = Long.parseLong(jwt.getSubject());
        return userService.getLinkedProviders(memberId);
    }
}
