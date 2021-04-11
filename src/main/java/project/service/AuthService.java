package project.service;

import project.dto.z_auth.LoginRequest;
import project.dto.z_auth.RegistrationRequest;
import project.dto.z_auth.RegistrationResponse;
import project.dto.z_auth.UserResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<UserResponse> checkUserAuthorization();

    ResponseEntity<UserResponse> login(LoginRequest request);

    ResponseEntity<RegistrationResponse> registration(RegistrationRequest request);
}
