package diploma.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

public class RegisterDto {
    private boolean result;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> errors;

    public boolean isResult() {
        return result;
    }

    public RegisterDto(boolean result, Map<String, String> errors) {
        this.result = result;
        this.errors = errors;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }
}
