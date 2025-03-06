package getToThePoint.unicatApi.mvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;
import getToThePoint.unicatApi.domain.dto.sign.SignInDto;
import getToThePoint.unicatApi.domain.dto.sign.SignUpDto;
import getToThePoint.unicatApi.application.service.AuthService;
import getToThePoint.unicatApi.presentation.controller.SignController;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SignController.class)
@Import(SignControllerTest.TestConfig.class)
class SignControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @TestConfiguration
    static class TestConfig {
        @Bean
        public AuthService authService() {
            return Mockito.mock(AuthService.class);
        }
        @Bean
        public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
            http.csrf(AbstractHttpConfigurer::disable)
                    .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
            return http.build();
        }
    }

    @Nested
    @DisplayName("SignUp Test Cases")
    class SignUpTest {
        @Test
        @DisplayName("Valid Sign Up")
        void testSignUpWithValidData() throws Exception {
            SignUpDto signUpDto = SignUpDto.builder().email("test@example.com").password("password").build();
            doNothing().when(authService).signUp(any(SignUpDto.class), any(HttpServletResponse.class));
            mockMvc.perform(post("/api/sign-up")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpDto)))
                    .andExpect(status().isCreated());
        }
        @Test
        @DisplayName("Empty Data Sign Up")
        void testSignUpWithEmptyData() throws Exception {
            SignUpDto signUpDto = SignUpDto.builder().build();
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required"))
                    .when(authService).signUp(any(SignUpDto.class), any(HttpServletResponse.class));
            mockMvc.perform(post("/api/sign-up")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpDto)))
                    .andExpect(status().isBadRequest());
        }
        @Test
        @DisplayName("Missing Email Sign Up")
        void testSignUpWithMissingEmail() throws Exception {
            SignUpDto signUpDto = SignUpDto.builder().password("password").build();
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is already in use"))
                    .when(authService).signUp(any(SignUpDto.class), any(HttpServletResponse.class));
            mockMvc.perform(post("/api/sign-up")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpDto)))
                    .andExpect(status().isBadRequest());
        }
        @Test
        @DisplayName("Missing Password Sign Up")
        void testSignUpWithMissingPassword() throws Exception {
            SignInDto signUpDto = SignInDto.builder().email("test@example.com").build();
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required"))
                    .when(authService).signUp(any(SignUpDto.class), any(HttpServletResponse.class));
            mockMvc.perform(post("/api/sign-up")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpDto)))
                    .andExpect(status().isBadRequest());
        }
        @Test
        @DisplayName("Duplicate Email Sign Up")
        void testSignUpWithDuplicateEmail() throws Exception {
            SignInDto signUpDto = SignInDto.builder().email("duplicate@example.com").password("password").build();
            doThrow(new ResponseStatusException(HttpStatus.CONFLICT, "Email is already in use"))
                    .when(authService).signUp(any(SignUpDto.class), any(HttpServletResponse.class));
            mockMvc.perform(post("/api/sign-up")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpDto)))
                    .andExpect(status().isConflict());
        }
    }

    @Nested
    @DisplayName("SignIn Test Cases")
    class SignInTest {
        @Test
        @DisplayName("Valid Sign In")
        void testSignInWithValidData() throws Exception {
            SignInDto signUpDto = SignInDto.builder().email("test@example.com").password("password").build();
            doNothing().when(authService).signIn(any(SignInDto.class), any(HttpServletResponse.class));
            mockMvc.perform(post("/api/sign-in")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpDto)))
                    .andExpect(status().isOk());
        }
        @Test
        @DisplayName("Missing Email Sign In")
        void testSignInWithMissingEmail() throws Exception {
            SignInDto signUpDto = SignInDto.builder().password("password").build();
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email is required"))
                    .when(authService).signIn(any(SignInDto.class), any(HttpServletResponse.class));
            mockMvc.perform(post("/api/sign-in")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpDto)))
                    .andExpect(status().isBadRequest());
        }
        @Test
        @DisplayName("Missing Password Sign In")
        void testSignInWithMissingPassword() throws Exception {
            SignInDto signUpDto = SignInDto.builder().email("test@example.com").build();
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password is required"))
                    .when(authService).signIn(any(SignInDto.class), any(HttpServletResponse.class));
            mockMvc.perform(post("/api/sign-in")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpDto)))
                    .andExpect(status().isBadRequest());
        }
        @Test
        @DisplayName("Invalid Credentials Sign In")
        void testSignInWithInvalidCredentials() throws Exception {
            SignInDto signUpDto = SignInDto.builder().email("test@example.com").password("wrongpassword").build();
            doThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password"))
                    .when(authService).signIn(any(SignInDto.class), any(HttpServletResponse.class));
            mockMvc.perform(post("/api/sign-in")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(signUpDto)))
                    .andExpect(status().isBadRequest());
        }
    }


}