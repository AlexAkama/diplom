package project.service;

import project.dto.auth.login.LoginRequest;
import project.dto.auth.registration.RegistrationRequest;
import project.dto.auth.registration.RegistrationResponse;
import project.dto.auth.user.AuthUserResponse;
import org.springframework.http.ResponseEntity;

public interface _AuthService {

    ResponseEntity<AuthUserResponse> checkUserAuthorization();

    ResponseEntity<AuthUserResponse> login(LoginRequest request);

    ResponseEntity<RegistrationResponse> registration(RegistrationRequest request);
}
