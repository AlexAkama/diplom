package project.dto.main;

import lombok.Getter;
import lombok.Setter;

/**
 * Базовый ответ результата
 */
@Getter
@Setter
public class AppResponse {

    /**
     * Отвтет запрос удачен/нет
     */
    private boolean result;

    /**
     * Конструктор базового ответа,<br>результат по умолчанию false
     */
    public AppResponse() {
        result = false;
    }

    public AppResponse ok() {
        setResult(true);
        return this;
    }

    public AppResponse bad() {
        return this;
    }

}
