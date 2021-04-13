package project.dto._auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import project.dto.main.AppResponse;

import java.util.Map;

/**
 * Если данные запроса на регистрацию верны - возвращет result=true
 * <br>Если возникли ошибки, то {@link RegistrationResponse#errors} с соответствующими полями:
 * <pre>    - "email": "Этот e-mail уже зарегистрирован"</pre>
 * <pre>    - "name": "Имя указано не верно"</pre>
 * <pre>    - "password": "Пароль короче 6-ти символов"</pre>
 * <pre>    -  captcha": "Код с картинки введен не верно"</pre>
 */
public class RegistrationResponse extends AppResponse {

    /**
     * Объект ошибок в классе {@link RegistrationResponse}
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> errors;


    // CONSTRUCTORS
    public RegistrationResponse() {
        this.setResult(true);
    }

    public RegistrationResponse(Map<String, String> errors) {
        this.setErrors(errors);
    }


    // GETTERS & SETTERS

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
        this.setResult(errors == null || errors.isEmpty());
    }
}
