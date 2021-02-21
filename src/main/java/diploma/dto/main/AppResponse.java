package diploma.dto.main;

/**
 * Базовый ответ результата
 */
public abstract class AppResponse {

    /**
     * Статус результата ответ
     */
    private boolean result;

    /**
     * Конструктор базового ответа,<br>результат по умолчанию null
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
}
