package taeniverse.unicatApi.mvc.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import taeniverse.unicatApi.mvc.model.dto.sign.SignInDto;
import taeniverse.unicatApi.mvc.model.dto.sign.SignUpDto;
import taeniverse.unicatApi.mvc.model.entity.Member;
import taeniverse.unicatApi.mvc.model.entity.Role;
import taeniverse.unicatApi.mvc.repository.MemberRepository;
import taeniverse.unicatApi.component.util.JwtUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final MessageSource messageSource;
    private final MemberService memberService;

    public void signUp(SignUpDto signUpDto, HttpServletResponse response) {
        if (memberRepository.existsByEmail(signUpDto.email())) {
            String errorMessage = messageSource.getMessage("error.email.in.use", null, "", LocaleContextHolder.getLocale());
            throw new ResponseStatusException(HttpStatus.CONFLICT, errorMessage);
        }

        Member member = memberService.create(signUpDto.email(), passwordEncoder.encode(signUpDto.password()));

        List<String> roleNames = member.getRoles().stream()
                .map(Role::getName)
                .toList();

        String token = jwtUtil.generateJwtToken(member.getId(), member.getEmail(), roleNames);
        jwtUtil.addJwtCookie(response, token);
    }

    public void signIn(SignInDto signInDto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(signInDto.email()).orElse(null);
        if (member == null || !passwordEncoder.matches(signInDto.password(), member.getPassword())) {
            String errorMessage = messageSource.getMessage("error.invalid.credentials", null, "", LocaleContextHolder.getLocale());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMessage);
        }

        List<String> roleNames = member.getRoles().stream()
                .map(Role::getName)
                .toList();

        String token = jwtUtil.generateJwtToken(member.getId(), member.getEmail(), roleNames);
        jwtUtil.addJwtCookie(response, token);
    }

    public void signOut(HttpServletResponse response) {
        jwtUtil.removeJwtCookie(response);
    }
}