package project.dto.main;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.util.Map;

@Getter
public class AppResponseWithErrors extends AppResponse{

    /**
     * Объект ошибок
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, String> errors;

    public AppResponseWithErrors() {
        setResult(true);
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
        this.setResult(errors == null || errors.isEmpty());
    }
}
