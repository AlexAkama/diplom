package project.dto.auth.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import project.dto.main.AppResponse;

/**
 * Ответ с данными пользователя,<br>по умолчанию result=false, user=null
 */
@NoArgsConstructor
@Getter
public class AuthResponse extends AppResponse {

    /**
     * Данные пользователя, исключаются если null
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthUserDto user;

    public AuthResponse(AuthUserDto user) {
        this.setResult(true);
        this.setUser(user);
    }

    public void setUser(AuthUserDto user) {
        this.setResult(true);
        this.user = user;
    }

}
