package project.service;

import project.dto._auth.LoginRequest;
import project.dto._auth.RegistrationRequest;
import project.dto._auth.RegistrationResponse;
import project.dto._auth.UserResponse;
import org.springframework.http.ResponseEntity;

public interface _AuthService {

    ResponseEntity<UserResponse> checkUserAuthorization();

    ResponseEntity<UserResponse> login(LoginRequest request);

    ResponseEntity<RegistrationResponse> registration(RegistrationRequest request);
}
