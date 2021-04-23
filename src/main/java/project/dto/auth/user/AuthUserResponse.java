package project.dto.auth.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import project.dto.main.AppResponse;
import project.model.User;

/**
 * Ответ с данными пользователя,<br>по умолчанию result=false, user=null
 */
public class AuthUserResponse extends AppResponse {

    /**
     * Данные пользователя, исключаются если null
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthUserDto user;


    public AuthUserResponse() {
    }

    public AuthUserResponse(User user) {
        this.setUser(user);
    }


    public AuthUserDto getUser() {
        return user;
    }

    public void setUser(User user) {
        setUser(new AuthUserDto(user));
    }

    public void setUser(AuthUserDto user) {
        this.user = user;
        this.setResult(user != null);
    }

}
