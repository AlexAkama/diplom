package diploma.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import diploma.dto.AuthUserDto;
import diploma.dto.main.AppResponse;

/**
 * Ответ с данными пользователя,<br>по умолчанию result=false, user=null
 */
public class UserResponse extends AppResponse {

    /**
     * Данные пользователя, исключаются если null
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthUserDto user;


    // GETTERS & SETTERS
    public AuthUserDto getUser() {
        return user;
    }

    public void setUser(AuthUserDto user) {
        this.user = user;
        this.setResult(user != null);
    }

}
