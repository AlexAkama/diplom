package project.dto.auth.registration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * Данные запроса на регистрацию:
 * <pre>    - Электронная почта</pre>
 * <pre>    - Пароль</pre>
 * <pre>    - Имя пользователя</pre>
 * <pre>    - Код капчи</pre>
 * <pre>    - Секретный код капчи</pre>
 */
@Getter
@Setter
public class RegistrationRequest {

    /**
     * Электронная почта
     */
    @JsonProperty("e_mail")
    private String email;

    /**
     * Пароль
     */
    private String password;

    /**
     * Имя пользователя
     */
    private String name;

    /**
     * Код капчи
     */
    @JsonProperty("captcha")
    private String code;

    /**
     * Секретный код капчи
     */
    @JsonProperty("captcha_secret")
    private String secret;

}
