package project.service.impementation;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.dto.auth.login.LoginRequest;
import project.dto.auth.registration.*;
import project.dto.auth.user.AuthResponse;
import project.dto.main.OkResponse;
import project.exception.UserNotFoundException;
import project.service.*;

import javax.servlet.http.*;
import java.security.Principal;

@Service
public class AuthServiceImpl implements _AuthService {

    private final _UserService userService;
    private final CaptchaService captchaService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(_UserService userService,
                           CaptchaService captchaService,
                           AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.captchaService = captchaService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseEntity<AuthResponse> checkUserAuthorization(Principal principal) throws UserNotFoundException {
        AuthResponse response = new AuthResponse();
        if (principal != null) {
            response = new AuthResponse(userService.createAuthUserDtoByEmail(principal.getName()));
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<AuthResponse> login(LoginRequest request) throws UserNotFoundException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        AuthResponse response = new AuthResponse(userService.createAuthUserDtoByEmail(request.getEmail()));
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RegistrationResponse> registration(RegistrationRequest request) {

        String email = request.getEmail();
        String password = request.getPassword();
        String name = request.getName();
        String code = request.getCode();
        String secret = request.getSecret();

        boolean emailCorrect = !userService.existByEmail(email);
        boolean captchaCorrect = captchaService.isCodeCorrect(code, secret);
        boolean nameCorrect = name.length() == name.replaceAll("[^A-Za-zА-Яа-яЁё\\s]+", "").length();
        boolean passwordCorrect = password.length() > 5;

        boolean registration = emailCorrect && nameCorrect && passwordCorrect && captchaCorrect;
        RegistrationResponse response = new RegistrationResponse();
        RegistrationErrorMap errors = new RegistrationErrorMap();
        if (registration) {
            userService.createAndSaveUser(name, email, password);
        } else {
            if (!emailCorrect) errors.addEmailError();
            if (!nameCorrect) errors.addNameError();
            if (!passwordCorrect) errors.addPasswordError();
            if (!captchaCorrect) errors.addCaptchaError();
            response.setErrors(errors.getErrors());
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<OkResponse> logout(HttpServletRequest request, HttpServletResponse response) {
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
        return ResponseEntity.ok(new OkResponse());
    }

}
