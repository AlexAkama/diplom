package project.dto.auth.login;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *  Данные запроса на вход: почта и пароль
 */
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


    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }


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
}
