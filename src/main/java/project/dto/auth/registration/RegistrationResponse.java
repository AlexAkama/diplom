package project.dto.auth.registration;

import project.dto.main.AppResponseWithErrors;

/**
 * Если данные запроса на регистрацию верны - возвращет result=true
 * <br>Если возникли ошибки, то заполняем {@link AppResponseWithErrors#errors объект} с соответствующими полями:
 * <pre>    - "email": "Этот e-mail уже зарегистрирован"</pre>
 * <pre>    - "name": "Имя указано не верно"</pre>
 * <pre>    - "password": "Пароль короче 6-ти символов"</pre>
 * <pre>    -  captcha": "Код с картинки введен не верно"</pre>
 */
public class RegistrationResponse extends AppResponseWithErrors {

    public RegistrationResponse() {
        setResult(true);
    }

}
