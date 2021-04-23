package project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import project.dto.auth.login.LoginRequest;
import project.dto.auth.registration.CaptchaDto;
import project.dto.auth.registration.RegistrationRequest;
import project.dto.auth.registration.RegistrationResponse;
import project.dto.auth.user.AuthUserResponse;
import project.service.CaptchaService;
import project.service._AuthService;

import java.io.IOException;

/** Контроллер авторизации */
@Controller
@RequestMapping("/api/auth")
public class AuthController {

    private final _AuthService authService;
    private final CaptchaService captchaService;

    public AuthController(_AuthService authService,
                          CaptchaService captchaService) {
        this.authService = authService;
        this.captchaService = captchaService;
    }

    /**
     * Метод возвращает давнные о пользователе, если он авторизован
     *
     * @return {@link AuthUserResponse}
     */
    @GetMapping("/check")
    public ResponseEntity<AuthUserResponse> checkUserAuthorization() {
        return authService.checkUserAuthorization();
    }

    /**
     * КАПЧА
     *
     * @return
     * @throws IOException
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
     * @return {@link AuthUserResponse}
     */
    @PostMapping("/login")
    public ResponseEntity<AuthUserResponse> login(
            @RequestBody LoginRequest request
    ) {
        return authService.login(request);
    }

}
