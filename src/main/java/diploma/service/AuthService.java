package diploma.service;

import diploma.dto.auth.LoginRequest;
import diploma.dto.auth.RegistrationRequest;
import diploma.dto.auth.RegistrationResponse;
import diploma.dto.auth.UserResponse;
import org.springframework.http.ResponseEntity;

public interface AuthService {

    ResponseEntity<UserResponse> checkUserAuthorization();

    ResponseEntity<UserResponse> login(LoginRequest request);

    ResponseEntity<RegistrationResponse> registration(RegistrationRequest request);
}
