package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.dto.auth.login.LoginRequest;
import project.dto.auth.registration.*;
import project.dto.auth.user.AuthResponse;
import project.dto.main.AppResponse;
import project.exception.NotFoundException;
import project.service.AuthService;
import project.service.CaptchaService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

/** Контроллер авторизации */
@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final CaptchaService captchaService;

    public AuthController(AuthService authService,
                          CaptchaService captchaService) {
        this.authService = authService;
        this.captchaService = captchaService;
    }

    /**
     * Метод возвращает давнные о пользователе, если он авторизован
     *
     * @return {@link AuthResponse}
     */
    @GetMapping("/check")
    public ResponseEntity<AuthResponse> checkUserAuthorization(Principal principal) throws NotFoundException {
        return authService.checkUserAuthorization(principal);
    }

    /**
     * КАПЧА
     */
    @GetMapping("/captcha")
    public ResponseEntity<CaptchaDto> getCaptcha() throws IOException {
        return captchaService.getCaptcha();
    }

    /**
     * Запрос на регистрацию пользователя
     * <br> если данные не прошли, проверку возврашется список ошибок
     *
     * @param request {@link RegistrationRequest}
     * @return {@link RegistrationResponse}
     */
    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registration(
            @RequestBody RegistrationRequest request
    ) {
        return authService.registration(request);
    }

    /**
     * Метод возвращает данные полязователя если данные для входа верны
     *
     * @param request {@link LoginRequest}
     * @return {@link AuthResponse}
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

    @GetMapping("/logout")
    public ResponseEntity<AppResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        return authService.logout(request, response);
    }

}
