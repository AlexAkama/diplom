package project.dto.main;

import java.util.HashMap;
import java.util.Map;

public abstract class AppErrorMap {

    private Map<String, String> errors;

    public AppErrorMap() {
        errors = new HashMap<>();
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, String> errors) {
        this.errors = errors;
    }

    public boolean isEmpty() {
        return getErrors().isEmpty();
    }

}
