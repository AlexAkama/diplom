package project.dto.auth.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import project.dto.main.AppResponse;

/**
 * Ответ с данными пользователя,<br>по умолчанию result=false, user=null
 */
public class AuthResponse extends AppResponse {

    /**
     * Данные пользователя, исключаются если null
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthUserDto user;

    public AuthResponse() {
    }

    public AuthResponse(AuthUserDto user) {
        this.setUser(user);
    }

    public AuthUserDto getUser() {
        return user;
    }

    public void setUser(AuthUserDto user) {
        this.user = user;
        this.setResult(user != null);
    }

}
