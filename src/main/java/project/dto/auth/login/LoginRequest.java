package project.dto.auth.login;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Данные запроса на вход: почта и пароль
 */
@AllArgsConstructor
@Getter
@Setter
public class LoginRequest {

    /**
     * Поле почты
     */
    @JsonProperty("e_mail")
    private String email;

    /**
     * Поле пароля
     */
    private String password;

}
