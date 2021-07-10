package project.dto.main;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public abstract class AppErrorMap {

    private Map<String, String> errors;

    protected AppErrorMap() {
        errors = new HashMap<>();
    }

    public boolean isEmpty() {
        return getErrors().isEmpty();
    }

}
