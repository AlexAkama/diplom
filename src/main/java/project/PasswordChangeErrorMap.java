package project;

import project.dto.main.AppErrorMap;

public class PasswordChangeErrorMap extends AppErrorMap {

    void addTimeError() {
        getErrors().put("code", "Ссылка для востановления устарела. <a href=\"/login/restore-password\">Запросить ссылку снова</a>");
    }

    void addPasswordError() {
        getErrors().put("password", "Пароль короче 6-ти символов");
    }

    void addCaptchaError() {
        getErrors().put("captcha", "Код с картинки введен не верно");
    }

}
