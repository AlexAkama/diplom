package diploma.service;

import diploma.dto.auth.RegistrationRequest;
import diploma.dto.auth.RegistrationResponse;
import diploma.dto.auth.UserResponse;
import diploma.dto.auth.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    ResponseEntity<UserResponse> checkUserAuthorization();

    ResponseEntity<UserResponse> login(LoginRequest request);

    ResponseEntity<RegistrationResponse> registration(RegistrationRequest request);
}
