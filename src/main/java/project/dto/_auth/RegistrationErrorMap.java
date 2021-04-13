package project.dto._auth;

import java.util.HashMap;
import java.util.Map;

/**
 * Конструктор ошибок в данных для регистрации пользователя
 */
public class RegistrationErrorMap {

    Map<String, String> errors;

    // CONSTRUCTORS
    public RegistrationErrorMap() {
        errors = new HashMap<>();
    }


    //METHODS

    public void addEmailError() {
        errors.put("email", "Этот e-mail уже зарегистрирован");
    }

    public void addNameError() {
        errors.put("name", "Имя указано не верно");
    }

    public void addPasswordError() {
        errors.put("password", "Пароль короче 6-ти символов");
    }

    public void addCaptchaError() {
        errors.put("captcha", "Код с картинки введен не верно");
    }


    // GETTERS & SETTERS

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
