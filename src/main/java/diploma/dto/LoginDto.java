package diploma.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

public class LoginDto {
    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private AuthUserDto user;

    public LoginDto() {
        result = false;
        user = null;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public AuthUserDto getUser() {
        return user;
    }

    public void setUser(AuthUserDto user) {
        this.user = user;
    }

}
