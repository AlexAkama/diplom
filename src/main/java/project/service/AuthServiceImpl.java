package project.service;

import project.dto.auth.*;
import project.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserService userService;
    private final CaptchaService captchaService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(UserService userService,
                           CaptchaService captchaService,
                           AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.captchaService = captchaService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public ResponseEntity<UserResponse> checkUserAuthorization() {
        UserResponse response = new UserResponse();
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponse> login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                ));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.findByEmail(request.getEmail());
        return ResponseEntity.ok(new UserResponse(user));
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

}
