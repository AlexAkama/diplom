package project.dto.password;

import project.dto.main.AppErrorMap;

public class PasswordChangeErrorMap extends AppErrorMap {

    public void addTimeError() {
        getErrors().put("code", "Ссылка для восстановления устарела. " +
                "<a href=\"/login/restore-password\">Запросить ссылку снова</a>");
    }

    public void addPasswordError() {
        getErrors().put("password", "Пароль короче 6-ти символов");
    }

    public void addCaptchaError() {
        getErrors().put("captcha", "Код с картинки введен не верно");
    }

}
