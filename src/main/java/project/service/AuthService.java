package project.service;

import project.dto.auth.login.LoginRequest;
import project.dto.auth.registration.RegistrationRequest;
import project.dto.auth.registration.RegistrationResponse;
import project.dto.auth.user.AuthResponse;
import org.springframework.http.ResponseEntity;
import project.dto.main.OkResponse;
import project.exception.UserNotFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

public interface AuthService {

    ResponseEntity<AuthResponse> checkUserAuthorization(Principal principal) throws UserNotFoundException;

    ResponseEntity<AuthResponse> login(LoginRequest request) throws UserNotFoundException;

    ResponseEntity<RegistrationResponse> registration(RegistrationRequest request);

    ResponseEntity<OkResponse> logout(HttpServletRequest request, HttpServletResponse response);
}
