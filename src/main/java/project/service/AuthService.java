package project.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.dto.auth.login.LoginRequest;
import project.dto.auth.registration.*;
import project.dto.auth.user.AuthResponse;
import project.dto.main.AppResponse;
import project.exception.NotFoundException;

import javax.servlet.http.*;
import java.security.Principal;

@Service
public class AuthService {

    private final UserService userService;
    private final CaptchaService captchaService;
    private final AuthenticationManager authenticationManager;

    /**
     * Минимальная длинна пароля
     */
    @Value("${config.password.minlength}")
    private int passwordMinLength;

    public AuthService(UserService userService,
                       CaptchaService captchaService,
                       AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.captchaService = captchaService;
        this.authenticationManager = authenticationManager;
    }

    public ResponseEntity<AuthResponse> checkUserAuthorization(Principal principal) throws NotFoundException {
        var response = new AuthResponse();
        if (principal != null) {
            response = new AuthResponse(userService.createAuthUserDtoByEmail(principal.getName()));
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<AuthResponse> login(LoginRequest request) {
        var response = new AuthResponse();
        try {
            var user = userService.createAuthUserDtoByEmail(request.getEmail());
            response.setUser(user);
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    ));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (NotFoundException ignore) {
            // в случае неудачи возвращается соответствующий ответ со статусом 200
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<RegistrationResponse> registration(RegistrationRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();
        String name = request.getName();
        String code = request.getCode();
        String secret = request.getSecret();

        boolean emailCorrect = emailIsCorrect(email);
        boolean captchaCorrect = captchaService.codeIsCorrect(code, secret);
        boolean nameCorrect = nameIsCorrect(name);
        boolean passwordCorrect = passwordIsCorrect(password);

        boolean registration = emailCorrect && nameCorrect && passwordCorrect && captchaCorrect;
        var response = new RegistrationResponse();
        if (registration) {
            userService.createAndSaveUser(name, email, password);
        } else {
            var errors = new RegistrationErrorMap();
            if (!emailCorrect) errors.addEmailError();
            if (!nameCorrect) errors.addNameError();
            if (!passwordCorrect) errors.addPasswordError();
            if (!captchaCorrect) errors.addCaptchaError();
            response.setErrors(errors.getErrors());
        }
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<AppResponse> logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        for (Cookie cookie : request.getCookies()) {
            cookie.setValue("");
            cookie.setPath("/");
            cookie.setMaxAge(0);
            response.addCookie(cookie);
        }
        return ResponseEntity.ok(new AppResponse().ok());
    }

    public boolean nameIsCorrect(String name) {
        return name.length() == name.replaceAll("[^A-Za-zА-Яа-яЁё\\s]+", "").length();
    }

    public boolean emailIsCorrect(String email) {
        return !userService.existByEmail(email);
    }

    public boolean passwordIsCorrect(String password) {
        return password.length() >= passwordMinLength;
    }

}
