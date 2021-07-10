package project.dto.auth.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import project.dto.main.AppResponse;

/**
 * Ответ с данными пользователя,<br>по умолчанию result=false, user=null
 */
@NoArgsConstructor
@Getter
@Setter
public class AuthResponse extends AppResponse {

    /**
     * Данные пользователя, исключаются если null
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthUserDto user;

    public AuthResponse(AuthUserDto user) {
        this.setUser(user);
    }

}
