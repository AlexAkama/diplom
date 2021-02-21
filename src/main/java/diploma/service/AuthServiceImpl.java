package diploma.service;

import diploma.dto.AuthUserDto;
import diploma.dto.auth.RegistrationRequest;
import diploma.dto.auth.RegistrationResponse;
import diploma.dto.auth.UserResponse;
import diploma.dto.auth.LoginRequest;
import diploma.model.User;
import org.springframework.http.ResponseEntity;

public class AuthServiceImpl implements AuthService {

    private final UserService userService;

    public AuthServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<UserResponse> checkUserAuthorization() {
        UserResponse response = new UserResponse();
        //FIXME --- ИМИТАЦИЯ ЗАПРОСА СОТОЯНИЯ АВТОРИЗАЦИИ ---
        if (true) {
            User user = userService.getTestUser("Jake");
            response.setUser(new AuthUserDto(user));
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<UserResponse> login(LoginRequest request) {
        UserResponse response = new UserResponse();
        //FIXME --- ИМИТАЦИЯ АВТОРИЗАЦИИ ---
        if (request.getEmail() != null && request.getPassword() != null &&
                request.getEmail().equals("gremcox@bk.ru") && request.getPassword().equals("12345678")) {
            User user = userService.getTestUser("Alex");
            response.setUser(new AuthUserDto(user));
        }
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RegistrationResponse> registration(RegistrationRequest request) {
        return null;
    }

}
