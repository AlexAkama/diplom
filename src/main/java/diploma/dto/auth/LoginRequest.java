package diploma.dto.auth;

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

    // CONSTRUCTORS
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

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
}
