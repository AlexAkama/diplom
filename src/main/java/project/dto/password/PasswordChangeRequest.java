package project.dto.password;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequest {

    private String code;
    private String password;
    private String captcha;
    @JsonProperty("captcha_secret")
    private String secret;
}
