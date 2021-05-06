package project.dto.main;

/**
 * Базовый ответ результата
 */
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


    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public AppResponse ok() {
        setResult(true);
        return this;
    }

    public AppResponse bad() {
        return this;
    }

}
