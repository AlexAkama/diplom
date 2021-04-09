package project.service;

import project.dto.auth.LoginRequest;
import project.dto.auth.RegistrationRequest;
import project.dto.auth.RegistrationResponse;
import project.dto.auth.UserResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<UserResponse> checkUserAuthorization();

    ResponseEntity<UserResponse> login(LoginRequest request);

    ResponseEntity<RegistrationResponse> registration(RegistrationRequest request);
}
