package project;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PasswordChangeRequest {

    private String code;
    private String password;
    private String captcha;
    @JsonProperty("captcha_secret")
    private String secret;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

}