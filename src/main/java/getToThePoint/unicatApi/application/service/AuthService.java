package getToThePoint.unicatApi.application.service;

import getToThePoint.unicatApi.common.util.JwtUtil;
import getToThePoint.unicatApi.domain.dto.sign.SignInDto;
import getToThePoint.unicatApi.domain.dto.sign.SignUpDto;
import getToThePoint.unicatApi.domain.entity.Member;
import getToThePoint.unicatApi.domain.repository.MemberRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        String token = jwtUtil.generateJwtToken(member.getId(), member.getEmail());
        jwtUtil.addJwtCookie(response, token);
    }

    public void signIn(SignInDto signInDto, HttpServletResponse response) {
        Member member = memberRepository.findByEmail(signInDto.email()).orElse(null);
        if (member == null || !passwordEncoder.matches(signInDto.password(), member.getPassword())) {
            String errorMessage = messageSource.getMessage("error.invalid.credentials", null, "", LocaleContextHolder.getLocale());
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, errorMessage);
        }

        String token = jwtUtil.generateJwtToken(member.getId(), member.getEmail());
        jwtUtil.addJwtCookie(response, token);
    }

    public void signOut(HttpServletResponse response) {
        jwtUtil.removeJwtCookie(response);
    }
}