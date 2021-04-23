package project.dto.auth.registration;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Данные запроса на регистрацию:
 * <pre>    - Электронная почта</pre>
 * <pre>    - Пароль</pre>
 * <pre>    - Имя пользователя</pre>
 * <pre>    - Код капчи</pre>
 * <pre>    - Секретный код капчи</pre>
 */
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


    // GETTERS & SETTERS

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
