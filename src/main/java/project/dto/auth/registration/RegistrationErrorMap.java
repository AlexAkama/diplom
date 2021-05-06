package project.dto.auth.registration;

import project.dto.main.AppErrorMap;

/**
 * Конструктор ошибок в данных для регистрации пользователя
 */
public class RegistrationErrorMap extends AppErrorMap {

    public void addEmailError() {
        getErrors().put("email", "Этот e-mail уже зарегистрирован");
    }

    public void addNameError() {
        getErrors().put("name", "Имя указано не верно");
    }

    public void addPasswordError() {
        getErrors().put("password", "Пароль короче 6-ти символов");
    }

    public void addCaptchaError() {
        getErrors().put("captcha", "Код с картинки введен не верно");
    }

}
